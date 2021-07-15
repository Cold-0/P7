package com.openclassroom.go4lunch.ViewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassroom.go4lunch.Model.DataView.SearchValidationDataView;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.NearbySearchResponse;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.Result;
import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.PlaceDetailsResponse;
import com.openclassroom.go4lunch.Repository.Repository;
import com.openclassroom.go4lunch.Utils.ObservableX;
import com.openclassroom.go4lunch.ViewModel.Utils.ViewModelX;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModelX {


    private final MutableLiveData<SearchValidationDataView> mSearchValidationDataViewMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<MarkerOptions> mMarkerMutableLiveData = new MutableLiveData<>();
    private final ObservableX mClearMapObservable = new ObservableX();
    private final ObservableX mZoomMapObservable = new ObservableX();
    private final ObservableX mAddRestaurantToList = new ObservableX();

    public SearchViewModel(@NonNull @NotNull Application application) {
        super(application);
        mSearchValidationDataViewMutableLiveData.observeForever(this::OnPredictionSelected);
    }

    public void setSearchValidationDataViewMutable(SearchValidationDataView searchValidationDataViewMutable) {
        this.mSearchValidationDataViewMutableLiveData.setValue(searchValidationDataViewMutable);
    }

    // Callback when prediction is selected
    private void OnPredictionSelected(@NotNull SearchValidationDataView searchValidationDataViewMutable) {
        // Get Details of the selected AutoComplete place (Get Position)
        Call<PlaceDetailsResponse> callDetails = mRepository.getService().getDetails(searchValidationDataViewMutable.prediction.getPlaceId());
        callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                onResponseNearbySearch(searchValidationDataViewMutable, response);
            }

            @Override
            public void onFailure(@NotNull Call<PlaceDetailsResponse> call, @NotNull Throwable t) {

            }
        });
    }

    private void onResponseNearbySearch(SearchValidationDataView data, Response<PlaceDetailsResponse> response) {
        mClearMapObservable.notifyObservers();
        Location loc = new Location("");
        if (response.body() != null && data.searchMethod == SearchValidationDataView.SearchMethod.PREDICTION) {
            Double lat = response.body().getResult().getGeometry().getLocation().getLat();
            Double lng = response.body().getResult().getGeometry().getLocation().getLng();
            loc.setLatitude(lat);
            loc.setLongitude(lng);
        } else {
            loc = getMyLocation();
        }
        // Add marker of choice position
        MarkerOptions userIndicator = new MarkerOptions()
                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                .title(response.body().getResult().getName())
                .snippet(response.body().getResult().getVicinity());

        mMarkerMutableLiveData.setValue(userIndicator);
        mZoomMapObservable.notifyObservers(loc);

        // Get Nearby Search Respond using the Details as location
        @SuppressLint("DefaultLocale") Call<NearbySearchResponse> callNearbySearch = data.searchMethod == SearchValidationDataView.SearchMethod.PREDICTION ?
                mRepository.getService().getNearbyByType(10000, String.format("%f,%f", loc.getLatitude(), loc.getLongitude()), "food") :
                mRepository.getService().getNearbyByKeyword(10000, String.format("%f,%f", loc.getLatitude(), loc.getLongitude()), data.searchString);

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

    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) requireApplication().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(requireApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location loc = new Location("");
            loc.setLatitude(0.0);
            loc.setLongitude(0.0);
            return loc;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }
}