package com.openclassroom.go4lunch.messages;

import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerAddMessage {
    private MarkerOptions markerOptions;
    private String placeID;

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public MarkerAddMessage setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
        return this;
    }

    public String getPlaceID() {
        return placeID;
    }

    public MarkerAddMessage setPlaceID(String placeID) {
        this.placeID = placeID;
        return this;
    }
}
