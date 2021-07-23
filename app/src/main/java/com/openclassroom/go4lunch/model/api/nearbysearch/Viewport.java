package com.openclassroom.go4lunch.model.api.nearbysearch;

import com.google.gson.annotations.SerializedName;

public class Viewport {
    @SerializedName("northeast")
    private LatitudeLongitude mNorthEast;

    @SerializedName("southwest")
    private LatitudeLongitude mSouthWest;

    public LatitudeLongitude getSouthWest() {
        return mSouthWest;
    }

    public void setSouthWest(LatitudeLongitude southWest) {
        mSouthWest = southWest;
    }

    public LatitudeLongitude getNorthEast() {
        return mNorthEast;
    }

    public void setNorthEast(LatitudeLongitude northEast) {
        mNorthEast = northEast;
    }
}
