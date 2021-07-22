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

    private static final String TAG = Repository.class.toString();

    public static Repository getRepository() {
        if (repository == null)
            repository = new Repository();
        return repository;
    }

    // ----------------
    // Instance
    // ----------------
    RetrofitService mRetrofitService;
    MutableLiveData<List<User>> mUserMutableLiveData;
    private final FirebaseFirestore mFirebaseFirestore;

    public RetrofitService getRetrofitService() {
        return mRetrofitService;
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
                            List<String> castedLikes = (List<String>) likes;

                            String id = document.getId();
                            userList.add(
                                    new User(
                                            document.getId(),
                                            (String) document.get("name"),
                                            castedLikes,
                                            (String) document.get("avatarUrl"),
                                            (String) document.get("eatingAt"),
                                            (String) document.get("eatingAtName")
                                    )
                            );
                        }

                        mUserMutableLiveData.setValue(userList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public FirebaseUser getCurrentUserFirebase() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public MutableLiveData<List<User>> getUsersListLiveData() {
        return mUserMutableLiveData;
    }


    Repository() {
        mRetrofitService = RetrofitInstance.getRetrofitInstance().create(RetrofitService.class);
        mUserMutableLiveData = new MutableLiveData<>();
        mUserMutableLiveData.setValue(new ArrayList<>());
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }
}
