package com.openclassroom.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.listener.OnToggledListener;

public class UserInfoViewModel extends ViewModelEX {

    // ----------------------------
    // Properties
    // ----------------------------
    MutableLiveData<User> mCurrentUser = new MutableLiveData<>();
    MutableLiveData<List<User>> mUserList;
    Observer<List<User>> mUserListObserver;

    // ----------------------------
    // Constructor
    // ----------------------------
    public UserInfoViewModel(@NonNull @NotNull Application application) {
        super(application);

        getRepository().updateUserList();

        mUserList = getRepository().getUsersListLiveData();
        mUserListObserver = users -> {
            FirebaseUser firebaseUser = getRepository().getCurrentFirebaseUser();
            for (User user : Objects.requireNonNull(getRepository().getUsersListLiveData().getValue())) {
                if (user.getUid().equals(firebaseUser.getUid()))
                    mCurrentUser.setValue(user);
            }
        };

        mUserList.observeForever(mUserListObserver);
    }

    // ----------------------------
    // Override
    // ----------------------------
    @Override
    protected void onCleared() {
        super.onCleared();
        mUserList.removeObserver(mUserListObserver);
    }

    // ----------------------------
    // Getter
    // ----------------------------
    @Nullable
    public FirebaseUser getCurrentFirebaseUser() {
        return getRepository().getCurrentFirebaseUser();
    }

    public MutableLiveData<User> getCurrentUser() {
        return mCurrentUser;
    }

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

    // ----------------------------
    // Methods
    // ----------------------------
    public void updateUserList() {
        getRepository().updateUserList();
    }
}
