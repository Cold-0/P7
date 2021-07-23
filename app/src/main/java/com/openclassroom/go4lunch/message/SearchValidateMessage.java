package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.api.autocomplete.Prediction;

import com.openclassroom.go4lunch.type.FragmentViewType;
import com.openclassroom.go4lunch.type.SearchType;

public class SearchValidateMessage {
    private Prediction prediction = null;
    private String searchString = null;
    private SearchType searchMethod = SearchType.PREDICTION;
    private FragmentViewType viewType = FragmentViewType.LIST;

    public Prediction getPrediction() {
        return prediction;
    }

    public SearchValidateMessage prediction(Prediction prediction) {
        this.prediction = prediction;
        return this;
    }

    public String getSearchString() {
        return searchString;
    }

    public SearchValidateMessage searchstring(String searchString) {
        this.searchString = searchString;
        return this;
    }

    public SearchType getSearchMethod() {
        return searchMethod;
    }

    public SearchValidateMessage searchmethod(SearchType searchMethod) {
        this.searchMethod = searchMethod;
        return this;
    }

    public FragmentViewType getViewType() {
        return viewType;
    }

    public SearchValidateMessage viewtype(FragmentViewType viewType) {
        this.viewType = viewType;
        return this;
    }
}