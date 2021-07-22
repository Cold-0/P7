package com.openclassroom.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.UserListUpdateState;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.viewmodel.Listener.OnUserListUpdateListener;

public class UserInfoViewModel extends ViewModelEX {
    MutableLiveData<User> currentUser = new MutableLiveData<>();
    MutableLiveData<List<User>> mUserList;
    Observer<List<User>> mUserListObserver;

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void updateUserList() {
        getRepository().updateUserList();
    }

    public void updateUserList(OnUserListUpdateListener listener) {
        getRepository().updateUserList();
        getRepository().getOnUpdateUsersList().addObserver((o, arg) -> {
            UserListUpdateState userListUpdateState = (UserListUpdateState) arg;
            listener.onUserListUpdated(userListUpdateState.currentUser, userListUpdateState.userList);
        });
    }

    public UserInfoViewModel(@NonNull @NotNull Application application) {
        super(application);

        getRepository().updateUserList();

        mUserList = getRepository().getUsersListLiveData();

        mUserListObserver = users -> {
            FirebaseUser firebaseUser = getRepository().getCurrentUserFirebase();
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
