package com.openclassroom.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkmatesViewModel extends ViewModelEX {

    private final MutableLiveData<List<User>> mUserList;

    public WorkmatesViewModel(@NonNull @NotNull Application application) {
        super(application);
        mUserList = getRepository().getUsersListLiveData();
        getRepository().updateUserList();

    }

    public LiveData<List<User>> getUserList() {
        return mUserList;
    }
}