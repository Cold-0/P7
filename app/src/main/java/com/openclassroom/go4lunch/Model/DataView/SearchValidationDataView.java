package com.openclassroom.go4lunch.Model.DataView;

import com.openclassroom.go4lunch.Model.AutocompleteAPI.Prediction;

import com.openclassroom.go4lunch.View.Activity.MainActivity.ViewTypes;

public class SearchValidationDataView {

    public enum SearchMethod {
        PREDICTION,
        SEARCH_STRING
    }

    public Prediction prediction = null;
    public String searchString = null;
    public SearchMethod searchMethod = SearchMethod.PREDICTION;
    public ViewTypes viewType = ViewTypes.LIST;
}