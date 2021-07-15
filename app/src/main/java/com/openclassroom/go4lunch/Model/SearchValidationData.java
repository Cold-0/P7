package com.openclassroom.go4lunch.Model;

import com.openclassroom.go4lunch.Model.AutocompleteAPI.Prediction;

import com.openclassroom.go4lunch.View.Activity.MainActivity.MainViewTypes;

public class SearchValidationData {

    public enum SearchMethod {
        PREDICTION,
        SEARCH_STRING
    }

    public Prediction prediction = null;
    public String searchString = null;
    public SearchMethod searchMethod = SearchMethod.PREDICTION;
    public MainViewTypes viewType = MainViewTypes.LIST;
}