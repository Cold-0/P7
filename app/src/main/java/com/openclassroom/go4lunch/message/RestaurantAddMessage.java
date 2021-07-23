package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResult;

import java.util.List;

public class RestaurantAddMessage {
    public NearbySearchResult result;
    public List<User> userList;
}
