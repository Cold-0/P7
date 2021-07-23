package com.openclassroom.go4lunch.listener;

import com.openclassroom.go4lunch.model.api.placedetails.DetailsResult;

public interface OnDetailListener {
    public void onResponse(DetailsResult detailsResult);
}
