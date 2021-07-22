package com.openclassroom.go4lunch.model.data;

import com.openclassroom.go4lunch.model.autocompleteapi.Prediction;

import com.openclassroom.go4lunch.view.activity.MainActivity.MainViewTypes;

public class SearchValidationData {

    public enum SearchMethod {
        PREDICTION,
        SEARCH_STRING,
        CLOSER
    }

    public Prediction prediction = null;
    public String searchString = null;
    public SearchMethod searchMethod = SearchMethod.PREDICTION;
    public MainViewTypes viewType = MainViewTypes.LIST;
}