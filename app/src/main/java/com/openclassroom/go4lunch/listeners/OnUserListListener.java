package com.openclassroom.go4lunch.listeners;

import com.openclassroom.go4lunch.models.User;

import java.util.List;

public interface OnUserListListener {
    void onResponse(User currentUser, List<User> userList);
}
