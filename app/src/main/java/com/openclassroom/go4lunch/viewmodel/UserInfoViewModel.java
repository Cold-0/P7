package com.openclassroom.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.openclassroom.go4lunch.listener.OnResponseListener;
import com.openclassroom.go4lunch.listener.OnUserListListener;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import com.openclassroom.go4lunch.listener.OnToggledListener;

public class UserInfoViewModel extends ViewModelEX {

    // ----------------------------
    // Properties
    // ----------------------------


    // ----------------------------
    // Constructor
    // ----------------------------
    public UserInfoViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    // ----------------------------
    // Override
    // ----------------------------
    @Override
    protected void onCleared() {
        super.onCleared();
    }

    // ----------------------------
    // Getter
    // ----------------------------
    public Boolean isCurrentUserLogged() {
        return getRepository().isCurrentUserLogged();
    }

    // ----------------------------
    // Call
    // ----------------------------
    public void callToggleLike(String placeID, OnToggledListener toggledLike) {
        getRepository().toggleLike(placeID, toggledLike);
    }

    public void callToggleEatingAt(String eatingAt, String eatingAtName, OnToggledListener toggled) {
        getRepository().toggleEatingAt(eatingAt, eatingAtName, toggled);
    }

    public void callUserList(OnUserListListener listener) {
        getRepository().callUserList(listener);
    }

    public void callCurrentUser(OnResponseListener<User> listener) {
        getRepository().callCurrentUser(listener);
    }

    public void updateUserOnFirebase() {
        getRepository().updateUserOnFirebase();
    }
}
