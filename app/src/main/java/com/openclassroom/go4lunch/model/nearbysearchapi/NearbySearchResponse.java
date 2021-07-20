package com.openclassroom.go4lunch.model.nearbysearchapi;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NearbySearchResponse {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("next_page_token")
    private String mNextPageToken;
    @SerializedName("results")
    private List<NearbySearchResult> mResultSearches;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public List<NearbySearchResult> getResults() {
        return mResultSearches;
    }

    public void setResults(List<NearbySearchResult> resultSearches) {
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
