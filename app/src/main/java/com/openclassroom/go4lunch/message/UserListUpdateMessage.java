package com.openclassroom.go4lunch.message;

import com.openclassroom.go4lunch.model.User;

import java.util.List;

public class UserListUpdateMessage {
    public List<User> userList;
    public User currentUser;
}
