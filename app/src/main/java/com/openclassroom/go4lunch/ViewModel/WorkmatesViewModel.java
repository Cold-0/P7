package com.openclassroom.go4lunch.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassroom.go4lunch.Model.User;
import com.openclassroom.go4lunch.ViewModel.Utils.ViewModelX;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkmatesViewModel extends ViewModelX {

    private final MutableLiveData<List<User>> mUserList;

    public WorkmatesViewModel(@NonNull @NotNull Application application) {
        super(application);
        mUserList = new MutableLiveData<>(new ArrayList<>(
                Arrays.asList(
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                        new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512")
                )));
    }

    public LiveData<List<User>> getUserList() {
        return mUserList;
    }
}