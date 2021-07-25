package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResult;

import java.util.List;

public class RestaurantAddMessage {
    private NearbySearchResult result;
    private List<User> userList;

    public NearbySearchResult getResult() {
        return result;
    }

    public RestaurantAddMessage setNearbySearchResult(NearbySearchResult result) {
        this.result = result;
        return this;
    }

    public List<User> getUserList() {
        return userList;
    }

    public RestaurantAddMessage setUserList(List<User> userList) {
        this.userList = userList;
        return this;
    }
}
