package com.openclassroom.go4lunch.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassroom.go4lunch.Model.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantListViewModel extends ViewModel {

    private final MutableLiveData<List<Restaurant>> mRestaurantList;

    public RestaurantListViewModel() {
        mRestaurantList = new MutableLiveData<>(new ArrayList<Restaurant>(
                Arrays.asList(
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant(),
                        new Restaurant()
                )));
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return mRestaurantList;
    }
}