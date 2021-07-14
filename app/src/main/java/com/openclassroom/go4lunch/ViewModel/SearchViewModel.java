package com.openclassroom.go4lunch.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassroom.go4lunch.Model.DataView.SearchValidationDataView;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.NearbySearchResponse;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.Result;
import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.PlaceDetailsResponse;
import com.openclassroom.go4lunch.Repository.Repository;
import com.openclassroom.go4lunch.Utils.ObservableX;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final MutableLiveData<SearchValidationDataView> mSearchValidationDataViewMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<MarkerOptions> mMarkerMutableLiveData = new MutableLiveData<>();
    private final ObservableX mClearMapObservable = new ObservableX();
    private final ObservableX mZoomMapObservable = new ObservableX();

    private final ObservableX mAddRestaurantToList = new ObservableX();

    public SearchViewModel(@NonNull @NotNull Application application) {
        super(application);
        mSearchValidationDataViewMutableLiveData.observeForever(this::OnPredictionSelected);
        mRepository = Repository.getRepository();
    }

    public void setSearchValidationDataViewMutable(SearchValidationDataView searchValidationDataViewMutableLiveData) {
        this.mSearchValidationDataViewMutableLiveData.setValue(searchValidationDataViewMutableLiveData);
    }

    // Callback when prediction is selected
    private void OnPredictionSelected(@NotNull SearchValidationDataView prediction) {
        // Get Details of the selected AutoComplete place (Get Position)
        Call<PlaceDetailsResponse> callDetails = mRepository.getService().getDetails(prediction.prediction.getPlaceId());
        callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                OnDetailResponseFromPredictionSelected(response);
            }

            @Override
            public void onFailure(@NotNull Call<PlaceDetailsResponse> call, @NotNull Throwable t) {

            }
        });
    }

    // Callback when detail from selected prediction is received
    private void OnDetailResponseFromPredictionSelected(@NotNull Response<PlaceDetailsResponse> response) {
        Location loc = new Location("");

        mClearMapObservable.notifyObservers();

        if (response.body() != null) {
            Double lat = response.body().getResult().getGeometry().getLocation().getLat();
            Double lng = response.body().getResult().getGeometry().getLocation().getLng();
            loc.setLatitude(lat);
            loc.setLongitude(lng);

            // Add marker of choice position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(response.body().getResult().getName())
                    .snippet(response.body().getResult().getVicinity());

            mMarkerMutableLiveData.setValue(userIndicator);

            mZoomMapObservable.notifyObservers(loc);

            // Get Nearby Search Respond using the Details as location
            @SuppressLint("DefaultLocale") Call<NearbySearchResponse> callNearbySearch = mRepository.getService().getNearbyByKeyword(10000, String.format("%f,%f", lat, lng), "food");
            callNearbySearch.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    OnNearbySearchResponseFromDetailResponse(response);
                }

                @Override
                public void onFailure(@NotNull Call<NearbySearchResponse> call, @NotNull Throwable t) {
                }
            });
        }
    }

    // Callback when nearby search from detail position received
    private void OnNearbySearchResponseFromDetailResponse(@NotNull Response<NearbySearchResponse> response) {
        assert response.body() != null;
        for (Result result : response.body().getResults()) {
            @SuppressLint("DefaultLocale") Call<PlaceDetailsResponse> callDetails = mRepository.getService().getDetails(result.getPlaceId());
            callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(@NotNull Call<PlaceDetailsResponse> call, @NotNull Response<PlaceDetailsResponse> response) {
                    OnDetailResponsePerEachNearbySearchResult(response);
                }

                @Override
                public void onFailure(@NotNull Call<PlaceDetailsResponse> call, @NotNull Throwable t) {

                }
            });
        }
    }

    // Callback for Details of each result of the nearby search
    private void OnDetailResponsePerEachNearbySearchResult(@NotNull Response<PlaceDetailsResponse> response) {
        if (response.body() != null) {
            Double lat = response.body().getResult().getGeometry().getLocation().getLat();
            Double lng = response.body().getResult().getGeometry().getLocation().getLng();
            LatLng loc = new LatLng(lat, lng);

            // Add marker of user's position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(loc)
                    .title(response.body().getResult().getName())
                    .snippet(response.body().getResult().getVicinity());

            mMarkerMutableLiveData.setValue(userIndicator);

            mAddRestaurantToList.notifyObservers(response.body().getResult());
        }
    }

    public ObservableX getClearMapObservable() {
        return mClearMapObservable;
    }

    public ObservableX getZoomMapObservable() {
        return mZoomMapObservable;
    }

    public MutableLiveData<MarkerOptions> getMarkerMutableLiveData() {
        return mMarkerMutableLiveData;
    }

    public MutableLiveData<SearchValidationDataView> getSearchValidationDataViewMutableLiveData() {
        return mSearchValidationDataViewMutableLiveData;
    }

    public ObservableX getAddRestaurantToList() {
        return mAddRestaurantToList;
    }
}