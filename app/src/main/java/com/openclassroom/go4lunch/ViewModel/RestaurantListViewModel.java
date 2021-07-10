package com.openclassroom.go4lunch.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.Result;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {
    private final MutableLiveData<Result> restaurantOrigin = new MutableLiveData<>();

    public void setRestaurantList(Result restaurantList) {
        this.restaurantOrigin.setValue(restaurantList);
    }

    public MutableLiveData<Result> getRestaurantList() {
        return restaurantOrigin;
    }
}