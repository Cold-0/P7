package com.openclassroom.go4lunch.listener;

import com.openclassroom.go4lunch.model.api.autocomplete.AutocompleteResponse;

public interface OnAutoCompleteListener {
    void onResponse(AutocompleteResponse autocompleteResponse);
}
