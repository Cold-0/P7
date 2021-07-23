package com.openclassroom.go4lunch.utils.ex;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.view.activity.RestaurantDetailActivity;

public abstract class ActivityEX extends AppCompatActivity {

    // --------------------
    // UTILS
    // --------------------
    protected void openDetailRestaurant(String placeID) {
        Intent sendStuff = new Intent(this, RestaurantDetailActivity.class);
        sendStuff.putExtra("placeID", placeID);
        startActivity(sendStuff);
    }

    protected Location getLastKnownCoarseLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            // Walk through each enabled location provider and return the first found, last-known location
            for (String thisLocProvider : locationManager.getProviders(true)) {
                Location lastKnown = locationManager.getLastKnownLocation(thisLocProvider);

                if (lastKnown != null) {
                    return lastKnown;
                }
            }
        } catch (SecurityException exception) {
            exception.printStackTrace();
        }
        // Always possible there's no means to determine location
        return null;
    }

    public ActivityEX() {

    }
}