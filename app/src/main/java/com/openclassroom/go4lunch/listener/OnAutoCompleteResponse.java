package com.openclassroom.go4lunch.listener;

import com.openclassroom.go4lunch.model.api.autocomplete.AutocompleteResponse;

public interface OnAutoCompleteResponse {
    void onResponse(AutocompleteResponse autocompleteResponse);
}
