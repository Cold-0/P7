package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.api.autocomplete.Prediction;

import com.openclassroom.go4lunch.type.FragmentViewType;
import com.openclassroom.go4lunch.type.SearchType;

public class SearchValidateMessage {
    public Prediction prediction = null;
    public String searchString = null;
    public SearchType searchMethod = SearchType.PREDICTION;
    public FragmentViewType viewType = FragmentViewType.LIST;
}