package com.openclassroom.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.message.UserListUpdateMessage;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.viewmodel.listener.OnToggled;
import com.openclassroom.go4lunch.viewmodel.listener.OnUserListUpdateListener;

public class UserInfoViewModel extends ViewModelEX {
    MutableLiveData<User> currentUser = new MutableLiveData<>();
    MutableLiveData<List<User>> mUserList;
    Observer<List<User>> mUserListObserver;

    @Nullable
    public FirebaseUser getCurrentFirebaseUser() {
        return getRepository().getCurrentFirebaseUser();
    }

    public Boolean isCurrentUserLogged() {
        return getRepository().isCurrentUserLogged();
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void updateUserList() {
        getRepository().updateUserList();
    }

    public void toggleLike(String placeID, OnToggled toggledLike) {
        getRepository().toggleLike(placeID, toggledLike);
    }

    public void toggleEatingAt(String eatingAt, String eatingAtName, OnToggled toggled) {
        getRepository().toggleEatingAt(eatingAt, eatingAtName, toggled);
    }

    public void updateUserList(OnUserListUpdateListener listener) {
        getRepository().updateUserList();
        getRepository().getOnUpdateUsersList().addObserver((o, arg) -> {
            UserListUpdateMessage userListUpdateState = (UserListUpdateMessage) arg;
            listener.onUserListUpdated(userListUpdateState.currentUser, userListUpdateState.userList);
        });
    }

    public UserInfoViewModel(@NonNull @NotNull Application application) {
        super(application);

        getRepository().updateUserList();

        mUserList = getRepository().getUsersListLiveData();

        mUserListObserver = users -> {
            FirebaseUser firebaseUser = getRepository().getCurrentFirebaseUser();
            for (User user : Objects.requireNonNull(getRepository().getUsersListLiveData().getValue())) {
                if (user.getUid().equals(firebaseUser.getUid()))
                    currentUser.setValue(user);
            }
        };

        mUserList.observeForever(mUserListObserver);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mUserList.removeObserver(mUserListObserver);
    }
}
