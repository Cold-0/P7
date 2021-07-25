package com.openclassroom.go4lunch.utils.ex;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.openclassroom.go4lunch.view.activity.RestaurantDetailActivity;

import java.util.List;

public abstract class ActivityEX extends AppCompatActivity {
    // ----------------------------
    // Intent
    // ----------------------------
    protected void openDetailRestaurant(String placeID) {
        Intent sendStuff = new Intent(this, RestaurantDetailActivity.class);
        sendStuff.putExtra("placeID", placeID);
        startActivity(sendStuff);
    }

    // ----------------------------
    // Getter
    // ----------------------------
    protected Location getCurrentLocation() {
        LocationManager mLocationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Location errorLocation = new Location("");
                errorLocation.setLatitude(0.0);
                errorLocation.setLongitude(0.0);
                return errorLocation;
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
        return bestLocation;
    }
}