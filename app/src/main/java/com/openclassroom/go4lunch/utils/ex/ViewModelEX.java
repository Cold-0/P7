package com.openclassroom.go4lunch.utils.ex;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;

import com.openclassroom.go4lunch.listeners.OnResponseListener;
import com.openclassroom.go4lunch.models.api.autocomplete.AutocompleteResponse;
import com.openclassroom.go4lunch.models.api.nearbysearch.NearbySearchResponse;
import com.openclassroom.go4lunch.models.api.placedetails.PlaceDetailsResponse;
import com.openclassroom.go4lunch.repository.Repository;
import com.openclassroom.go4lunch.listeners.OnUserListListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    private Location errorLocation() {
        Location errorLocation = new Location("");
        errorLocation.setLatitude(0.0);
        errorLocation.setLongitude(0.0);
        return errorLocation;
    }

    public Location getMyLocation() {
        LocationManager mLocationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return errorLocation();
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return errorLocation();
        }
        return bestLocation;
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
