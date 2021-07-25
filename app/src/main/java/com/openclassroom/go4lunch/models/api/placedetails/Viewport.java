package com.openclassroom.go4lunch.models.api.placedetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Viewport implements Serializable {

    @SerializedName("northeast")
    private LatitudeLongitude mNortheast;
    @SerializedName("southwest")
    private LatitudeLongitude mSouthwest;

    public LatitudeLongitude getNortheast() {
        return mNortheast;
    }

    public void setNortheast(LatitudeLongitude northeast) {
        mNortheast = northeast;
    }

    public LatitudeLongitude getSouthwest() {
        return mSouthwest;
    }

    public void setSouthwest(LatitudeLongitude southwest) {
        mSouthwest = southwest;
    }

}
