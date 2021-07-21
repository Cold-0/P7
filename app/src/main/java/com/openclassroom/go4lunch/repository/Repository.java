package com.openclassroom.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
                            Object likes = document.get("likes");
                            List<String> castedLikes = null;
                            if (likes instanceof List)
                                castedLikes = (List<String>) likes;
                            userList.add(
                                    new User(
                                            document.getId(),
                                            (String) document.get("name"),
                                            castedLikes,
                                            (String) document.get("avatarUrl"),
                                            (String) document.get("eatingAt")
                                    )
                            );
                        }

                        mUserMutableLiveData.setValue(userList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
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
