package com.openclassroom.go4lunch.models.api.placedetails;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsResponse implements Serializable {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("result")
    private PlaceDetailsResult mResult;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public PlaceDetailsResult getResult() {
        return mResult;
    }

    public void setResult(PlaceDetailsResult result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
