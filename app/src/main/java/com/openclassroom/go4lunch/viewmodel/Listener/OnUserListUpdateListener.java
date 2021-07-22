package com.openclassroom.go4lunch.viewmodel.Listener;

import com.openclassroom.go4lunch.model.User;

import java.util.List;

public interface OnUserListUpdateListener {
    void onUserListUpdated(User currentUser, List<User> userList);
}
