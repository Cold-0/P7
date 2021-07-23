package com.openclassroom.go4lunch.listener;

import com.openclassroom.go4lunch.model.User;

import java.util.List;

public interface OnUserListUpdateListener {
    void onUserListUpdated(User currentUser, List<User> userList);
}
