package com.openclassroom.go4lunch.Model.NearbySearchAPI;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NearbySearchResponse {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("next_page_token")
    private String mNextPageToken;
    @SerializedName("results")
    private List<Result> mResultSearches;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public List<Result> getResults() {
        return mResultSearches;
    }

    public void setResults(List<Result> resultSearches) {
        mResultSearches = resultSearches;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        mNextPageToken = nextPageToken;
    }
}
