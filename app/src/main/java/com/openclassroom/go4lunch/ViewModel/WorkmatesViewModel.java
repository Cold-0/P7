package com.openclassroom.go4lunch.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassroom.go4lunch.Model.Restaurant;
import com.openclassroom.go4lunch.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkmatesViewModel extends ViewModel {

    private final MutableLiveData<List<User>> mUserList;

    public WorkmatesViewModel() {
        mUserList = new MutableLiveData<>(new ArrayList<>(
                Arrays.asList(
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User(),
                        new User()
                )));
    }

    public LiveData<List<User>> getUserList() {
        return mUserList;
    }
}