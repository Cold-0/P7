package com.openclassroom.go4lunch.utils.ex;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.openclassroom.go4lunch.view.activity.RestaurantDetailActivity;

public abstract class FragmentEX extends Fragment {

    protected void openDetailRestaurant(Activity activity, String placeID) {
        Intent sendStuff = new Intent(activity, RestaurantDetailActivity.class);
        sendStuff.putExtra("placeID", placeID);
        startActivity(sendStuff);
    }

    protected Location getLastKnowLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
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

    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location loc = new Location("");
            loc.setLatitude(0.0);
            loc.setLongitude(0.0);
            return loc;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    public FragmentEX() {

    }
}
