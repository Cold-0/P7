package com.openclassroom.go4lunch.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassroom.go4lunch.model.SearchValidationData;
import com.openclassroom.go4lunch.model.nearbysearchapi.NearbySearchResponse;
import com.openclassroom.go4lunch.model.nearbysearchapi.NearbySearchResult;
import com.openclassroom.go4lunch.model.placedetailsapi.PlaceDetailsResponse;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.utils.ex.ObservableEX;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModelEX {

    public static class MarkerState {
        public MarkerOptions markerOptions;
        public String placeID;
    }

    private final MutableLiveData<SearchValidationData> mSearchValidationDataViewMutableLiveData = new MutableLiveData<>();

    private final ObservableEX mClearMapObservable = new ObservableEX();
    private final ObservableEX mZoomMapObservable = new ObservableEX();
    private final ObservableEX mAddRestaurantToList = new ObservableEX();
    private final ObservableEX mClearRestaurantList = new ObservableEX();
    private final ObservableEX mAddMapMarker = new ObservableEX();

    // ------------------------
    // Getter
    // ------------------------
    public MutableLiveData<SearchValidationData> getSearchValidationDataViewMutableLiveData() {
        return mSearchValidationDataViewMutableLiveData;
    }

    public ObservableEX getClearMapObservable() {
        return mClearMapObservable;
    }

    public ObservableEX getZoomMapObservable() {
        return mZoomMapObservable;
    }

    public ObservableEX getAddRestaurantToList() {
        return mAddRestaurantToList;
    }

    public ObservableEX getClearRestaurantList() {
        return mClearRestaurantList;
    }

    // ------------------------
    // Setter
    // ------------------------
    public void setSearchValidationDataViewMutable(SearchValidationData searchValidationDataViewMutable) {
        this.mSearchValidationDataViewMutableLiveData.setValue(searchValidationDataViewMutable);
    }

    // ------------------------
    // Constructor
    // ------------------------
    public SearchViewModel(
            @NonNull @NotNull Application application) {
        super(application);
        mSearchValidationDataViewMutableLiveData.observeForever(this::OnSearchLaunch);
    }

    // ------------------------
    // Callback
    // ------------------------
    private void OnSearchLaunch(@NotNull SearchValidationData searchValidationDataView) {
        // Get Details of the selected AutoComplete place (Get Position)
        if (searchValidationDataView.searchMethod == SearchValidationData.SearchMethod.PREDICTION) {
            Call<PlaceDetailsResponse> callDetails = getRepository().getRetrofitService().getDetails(searchValidationDataView.prediction.getPlaceId());
            callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                    onResponseNearbySearch(searchValidationDataView, response);
                }

                @Override
                public void onFailure(@NotNull Call<PlaceDetailsResponse> call, @NotNull Throwable t) {

                }
            });
        } else {
            onResponseNearbySearch(searchValidationDataView, null);
        }
    }

    private void onResponseNearbySearch(SearchValidationData data, Response<PlaceDetailsResponse> response) {
        mClearMapObservable.notifyObservers();
        Location loc = new Location("");
        if (response != null && response.body() != null && data.searchMethod == SearchValidationData.SearchMethod.PREDICTION) {
            Double lat = response.body().getResult().getGeometry().getLocation().getLat();
            Double lng = response.body().getResult().getGeometry().getLocation().getLng();
            loc.setLatitude(lat);
            loc.setLongitude(lng);
            // Add marker of choice position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .title(response.body().getResult().getName())
                    .snippet(response.body().getResult().getVicinity());


            MarkerState state = new MarkerState();
            state.markerOptions = userIndicator;
            state.placeID = response.body().getResult().getPlaceId();
            mAddMapMarker.notifyObservers(state);

        } else {
            loc = getMyLocation();
        }

        mZoomMapObservable.notifyObservers(loc);

        // Get Nearby Search Respond using the Details as location
        Call<NearbySearchResponse> callNearbySearch = null;

        if (data.searchMethod == SearchValidationData.SearchMethod.PREDICTION || data.searchMethod == SearchValidationData.SearchMethod.CLOSER) {
            callNearbySearch = getRepository().getRetrofitService().getNearbyByType(10000, String.format(Locale.CANADA, getApplication().getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude()), "restaurant");
        } else if (data.searchMethod == SearchValidationData.SearchMethod.SEARCH_STRING) {
            callNearbySearch = getRepository().getRetrofitService().getNearbyByKeyword(10000, String.format(Locale.CANADA, getApplication().getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude()), data.searchString);
        }

        assert callNearbySearch != null;
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
        mClearRestaurantList.notifyObservers();
        for (NearbySearchResult result : response.body().getResults()) {
            if (response.body() != null) {
                Double lat = result.getGeometry().getLocation().getLat();
                Double lng = result.getGeometry().getLocation().getLng();
                LatLng loc = new LatLng(lat, lng);

                // Add marker of user's position
                MarkerOptions userIndicator = new MarkerOptions()
                        .position(loc)
                        .title(result.getName())
                        .snippet(result.getVicinity());


                MarkerState state = new MarkerState();
                state.markerOptions = userIndicator;
                state.placeID = result.getPlaceId();
                mAddMapMarker.notifyObservers(state);

                mAddRestaurantToList.notifyObservers(result);
            }
        }
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

    @Override
    protected void onCleared() {
        super.onCleared();
        mClearMapObservable.deleteObservers();
        mAddRestaurantToList.deleteObservers();
        mClearRestaurantList.deleteObservers();
        mZoomMapObservable.deleteObservers();
    }

    public ObservableEX getAddMapMarker() {
        return mAddMapMarker;
    }
}