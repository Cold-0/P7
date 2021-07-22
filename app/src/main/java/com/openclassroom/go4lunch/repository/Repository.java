package com.openclassroom.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.data.UserListUpdateData;
import com.openclassroom.go4lunch.repository.retrofit.RetrofitInstance;
import com.openclassroom.go4lunch.repository.retrofit.RetrofitService;
import com.openclassroom.go4lunch.utils.ex.ObservableEX;
import com.openclassroom.go4lunch.viewmodel.listener.OnToggled;

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
    private final RetrofitService mRetrofitService;
    private final FirebaseFirestore mFirebaseFirestore;

    private final MutableLiveData<List<User>> mUserMutableLiveData;
    private final MutableLiveData<User> mCurrentUser;

    public ObservableEX getOnUpdateUsersList() {
        return mOnUpdateUsersList;
    }

    private final ObservableEX mOnUpdateUsersList;

    public void toggleEatingAt(String placeID, String placeName, OnToggled toggledEatingAt) {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = mCurrentUser.getValue();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (document.getId().equals(user.getUid())) {
                                String eatingAt = (String) document.get("eatingAt");
                                String eatingAtName = (String) document.get("eatingAtName");

                                boolean isIn = false;
                                if (eatingAt.equals(placeID)) {
                                    isIn = true;
                                }
                                if (isIn) {
                                    eatingAt = "";
                                    eatingAtName = "";
                                } else {
                                    eatingAt = placeID;
                                    eatingAtName = placeName;
                                }
                                boolean finalWasIn = isIn;
                                mFirebaseFirestore.collection("users")
                                        .document(user.getUid())
                                        .update(
                                                "eatingAt", eatingAt,
                                                "eatingAtName", eatingAtName
                                        )
                                        .addOnSuccessListener(aVoid -> {
                                            toggledEatingAt.onToggled(!finalWasIn);
                                        });
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void toggleLike(String placeID, OnToggled toggledLike) {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = mCurrentUser.getValue();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (document.getId().equals(user.getUid())) {
                                List<String> likesList = (List<String>) document.get("likes");
                                boolean wasIn = false;
                                for (String like : likesList) {
                                    if (like.equals(placeID)) {
                                        likesList.remove(like);
                                        wasIn = true;
                                        break;
                                    }
                                }
                                if (!wasIn) {
                                    likesList.add(placeID);
                                }

                                boolean finalWasIn = wasIn;
                                mFirebaseFirestore.collection("users")
                                        .document(user.getUid())
                                        .update(
                                                "likes", likesList
                                        )
                                        .addOnSuccessListener(aVoid -> {
                                            toggledLike.onToggled(!finalWasIn);
                                        });
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
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
                            List<String> castedLikes = (List<String>) document.get("likes");
                            String newUserUID = document.getId();
                            User newUser = new User(
                                    newUserUID,
                                    (String) document.get("name"),
                                    castedLikes,
                                    (String) document.get("avatarUrl"),
                                    (String) document.get("eatingAt"),
                                    (String) document.get("eatingAtName")
                            );
                            userList.add(newUser);
                            if (newUserUID.equals(getCurrentUserFirebase().getUid())) {
                                mCurrentUser.setValue(newUser);
                            }
                        }

                        mUserMutableLiveData.setValue(userList);
                        UserListUpdateData userListUpdateState = new UserListUpdateData();
                        userListUpdateState.currentUser = mCurrentUser.getValue();
                        userListUpdateState.userList = mUserMutableLiveData.getValue();
                        mOnUpdateUsersList.notifyObservers(userListUpdateState);

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    // ---------------
    // Getter
    // ---------------
    public RetrofitService getRetrofitService() {
        return mRetrofitService;
    }

    public FirebaseUser getCurrentUserFirebase() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public MutableLiveData<List<User>> getUsersListLiveData() {
        return mUserMutableLiveData;
    }

    public MutableLiveData<User> getCurrentUser() {
        return mCurrentUser;
    }

    // ---------------
    // Constructor
    // ---------------
    Repository() {
        mRetrofitService = RetrofitInstance.getRetrofitInstance().create(RetrofitService.class);
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        mOnUpdateUsersList = new ObservableEX();
        mUserMutableLiveData = new MutableLiveData<>();
        mUserMutableLiveData.setValue(new ArrayList<>());
        mCurrentUser = new MutableLiveData<>();
    }
}
