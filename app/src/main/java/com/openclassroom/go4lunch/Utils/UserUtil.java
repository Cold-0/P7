package com.openclassroom.go4lunch.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserUtil {
    // --------------------------
    // Static
    // --------------------------

    final static String TAG = UserUtil.class.toString();

    static UserUtil instance;

    public static UserUtil getInstance() {
        if (instance == null)
            instance = new UserUtil();
        return instance;
    }

    // --------------------------
    // Instance
    // --------------------------

    private final FirebaseFirestore mFirebaseFirestore;

    UserUtil() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void addUser(String user_id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", user_id);
        user.put("likes", new ArrayList<String>());
        mFirebaseFirestore.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void getAllUsers() {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
