package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.api.autocomplete.Prediction;
import com.openclassroom.go4lunch.type.FragmentViewType;
import com.openclassroom.go4lunch.type.SearchType;

public class SearchValidateMessage {
    private Prediction mPrediction = null;
    private String mSearchString = null;
    private SearchType mSearchType = SearchType.PREDICTION;

    public Prediction getPrediction() {
        return mPrediction;
    }

    public SearchValidateMessage setPrediction(Prediction prediction) {
        this.mPrediction = prediction;
        return this;
    }

    public String getSearchString() {
        return mSearchString;
    }

    public SearchValidateMessage setSearchString(String searchString) {
        this.mSearchString = searchString;
        return this;
    }

    public SearchType getSearchType() {
        return mSearchType;
    }

    public SearchValidateMessage setSearchMethod(SearchType searchMethod) {
        this.mSearchType = searchMethod;
        return this;
    }
}