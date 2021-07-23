package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.api.autocomplete.Prediction;

import com.openclassroom.go4lunch.view.ViewTypeTabEnum;

public class SearchValidateMessage {

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