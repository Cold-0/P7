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

import com.openclassroom.go4lunch.repository.Repository;
import com.openclassroom.go4lunch.listener.OnDetailResponse;
import com.openclassroom.go4lunch.listener.OnAutoCompleteResponse;
import com.openclassroom.go4lunch.listener.OnUserListUpdateListener;

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
    public void callDetail(String placeID, OnDetailResponse onDetailResponse) {
        mRepository.callDetail(placeID, onDetailResponse);
    }

    public void callAutocomplete(String text, String localisation, String type, OnAutoCompleteResponse onAutoCompleteResponse) {
        mRepository.callAutocomplete(text, localisation, type, onAutoCompleteResponse);
    }

    public void callUserList(OnUserListUpdateListener listener) {
        mRepository.callUserList(listener);
    }

}
