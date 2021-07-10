package com.openclassroom.go4lunch.Model.NearbySearchAPI;

import com.google.gson.annotations.SerializedName;

public class LatitudeLongitude {
    @SerializedName("lat")
    Double mLatitude;

    @SerializedName("lng")
    Double mLongitude;

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

}
