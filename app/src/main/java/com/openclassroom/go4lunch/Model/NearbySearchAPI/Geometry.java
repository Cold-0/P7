package com.openclassroom.go4lunch.Model.NearbySearchAPI;

import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    private Location mLocation;
    @SerializedName("viewport")
    private Viewport mViewport;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

}
