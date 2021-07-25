package com.openclassroom.go4lunch.utils.ex;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;

import com.openclassroom.go4lunch.listener.OnResponseListener;
import com.openclassroom.go4lunch.model.api.autocomplete.AutocompleteResponse;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResponse;
import com.openclassroom.go4lunch.model.api.placedetails.PlaceDetailsResponse;
import com.openclassroom.go4lunch.repository.Repository;
import com.openclassroom.go4lunch.listener.OnUserListListener;

import org.jetbrains.annotations.NotNull;

abstract public class ViewModelEX extends AndroidViewModel {
    // --------------------
    // Static
    // --------------------
    static private final Repository mRepository = Repository.getRepository();

    // --------------------
    // Constructor
    // --------------------
    public ViewModelEX(@NonNull @NotNull Application application) {
        super(application);
    }

    // --------------------
    // Getter
    // --------------------
    protected Repository getRepository() {
        return mRepository;
    }

    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location loc = new Location("");
            loc.setLatitude(0.0);
            loc.setLongitude(0.0);
            return loc;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    // --------------------
    // Caller
    // --------------------
    public void callPlaceDetails(String placeID, OnResponseListener<PlaceDetailsResponse> listener) {
        mRepository.callPlaceDetails(placeID, listener);
    }

    public void callAutocomplete(String text, String localisation, String type, OnResponseListener<AutocompleteResponse> listener) {
        mRepository.callAutocomplete(text, localisation, type, listener);
    }

    public void callNearbySearchByType(String location, String type, OnResponseListener<NearbySearchResponse> listener) {
        mRepository.callNearbySearchByType(location, type, listener);
    }

    public void callNearbySearchByKeyword(String location, String keyword, OnResponseListener<NearbySearchResponse> listener) {
        mRepository.callNearbySearchByKeyword(location, keyword, listener);
    }

    public void callUserList(OnUserListListener listener) {
        mRepository.callUserList(listener);
    }

}
