package com.openclassroom.go4lunch.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassroom.go4lunch.Model.User;

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

    public MutableLiveData<List<User>> getUsersListLiveData() {
//
//        mFirebaseFirestore.collection("users")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        mUserMutableLiveData.getValue().clear();
//                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                            document
//                            mUserMutableLiveData.getValue().add();
//                        }
//                        mUserMutableLiveData.notifyAll();
//                    } else {
//                        Log.w(TAG, "Error getting documents.", task.getException());
//                    }
//                });
//
//        return mUserMutableLiveData;
        return mUserMutableLiveData;
    }


    Repository() {
        service = RetrofitInstance.getRetrofitInstance().create(RetrofitService.class);
        mUserMutableLiveData = new MutableLiveData<>();
        mUserMutableLiveData.setValue(new ArrayList<>());
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }
}
