package com.openclassroom.go4lunch.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;

import com.openclassroom.go4lunch.listeners.OnResponseListener;
import com.openclassroom.go4lunch.listeners.OnUserListListener;
import com.openclassroom.go4lunch.models.User;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import com.openclassroom.go4lunch.listeners.OnToggledListener;

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
        getRepository().callToggleLike(placeID, toggledLike);
    }

    public void callToggleEatingAt(String eatingAt, String eatingAtName, OnToggledListener toggled) {
        getRepository().callToggleEatingAt(eatingAt, eatingAtName, toggled);
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
