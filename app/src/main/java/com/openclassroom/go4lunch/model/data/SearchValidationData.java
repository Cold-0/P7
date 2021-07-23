package com.openclassroom.go4lunch.model.data;

import com.openclassroom.go4lunch.model.autocompleteapi.Prediction;

import com.openclassroom.go4lunch.view.ViewTypeTabEnum;

public class SearchValidationData {

    public enum SearchMethod {
        PREDICTION,
        SEARCH_STRING,
        CLOSER
    }

    public Prediction prediction = null;
    public String searchString = null;
    public SearchMethod searchMethod = SearchMethod.PREDICTION;
    public ViewTypeTabEnum viewType = ViewTypeTabEnum.LIST;
}