package com.openclassroom.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.repository.retrofit.RetrofitInstance;
import com.openclassroom.go4lunch.repository.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Repository {

    // ----------------
    // Singleton
    // ----------------
    static Repository repository;

    private static String TAG = Repository.class.toString();

    public static Repository getRepository() {
        if (repository == null)
            repository = new Repository();
        return repository;
    }

    // ----------------
    // Instance
    // ----------------
    RetrofitService service;
    MutableLiveData<List<User>> mUserMutableLiveData;
    private final FirebaseFirestore mFirebaseFirestore;

    public RetrofitService getService() {
        return service;
    }

    public void updateUserList() {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> userList = mUserMutableLiveData.getValue();
                        assert userList != null;
                        userList.clear();

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.e(TAG, "getUsersListLiveData: " + document.getId());
                            userList.add(new User(1, "Bob", new ArrayList<>(), "https://i.pravatar.cc/300"));
                        }

                        mUserMutableLiveData.setValue(userList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public MutableLiveData<List<User>> getUsersListLiveData() {
        return mUserMutableLiveData;
    }


    Repository() {
        service = RetrofitInstance.getRetrofitInstance().create(RetrofitService.class);
        mUserMutableLiveData = new MutableLiveData<>();
        mUserMutableLiveData.setValue(new ArrayList<>());
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }
}
