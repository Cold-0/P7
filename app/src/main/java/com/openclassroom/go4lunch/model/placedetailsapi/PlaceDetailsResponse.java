package com.openclassroom.go4lunch.model.placedetailsapi;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsResponse implements Serializable {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("result")
    private DetailsResult mResult;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public DetailsResult getResult() {
        return mResult;
    }

    public void setResult(DetailsResult result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
