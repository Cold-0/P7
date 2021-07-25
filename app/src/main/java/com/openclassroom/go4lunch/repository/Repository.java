package com.openclassroom.go4lunch.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassroom.go4lunch.listener.OnResponseListener;
import com.openclassroom.go4lunch.listener.OnUserListListener;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.api.autocomplete.AutocompleteResponse;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResponse;
import com.openclassroom.go4lunch.model.api.placedetails.PlaceDetailsResponse;
import com.openclassroom.go4lunch.repository.retrofit.RetrofitInstance;
import com.openclassroom.go4lunch.repository.retrofit.RetrofitService;
import com.openclassroom.go4lunch.listener.OnToggledListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    // Instance Properties
    // ----------------
    private final RetrofitService mRetrofitService;
    private final FirebaseFirestore mFirebaseFirestore;
    private final List<User> mUserMutableLiveDat = new ArrayList<>();
    private User mCurrentUser;

    // ---------------
    // Constructor
    // ---------------
    Repository() {
        mRetrofitService = RetrofitInstance.getRetrofitInstance().create(RetrofitService.class);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    // --------------
    // Retrofit Call
    // --------------
    public RetrofitService getRetrofitService() {
        return mRetrofitService;
    }

    public void callAutocomplete(String text, String localisation, String type, OnResponseListener<AutocompleteResponse> listener) {
        Call<AutocompleteResponse> call = repository.getRetrofitService().getAutocomplete(text, 5000, localisation, type);
        call.enqueue(new Callback<AutocompleteResponse>() {
            @Override
            public void onResponse(@NonNull Call<AutocompleteResponse> call, @NonNull Response<AutocompleteResponse> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AutocompleteResponse> call, @NonNull Throwable t) {

            }
        });
    }

    public void callPlaceDetails(String placeID, OnResponseListener<PlaceDetailsResponse> listener) {
        Call<PlaceDetailsResponse> callDetails = repository.getRetrofitService().getDetails(placeID);
        callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NotNull Call<PlaceDetailsResponse> call, @NotNull Response<PlaceDetailsResponse> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<PlaceDetailsResponse> call, @NotNull Throwable t) {

            }
        });
    }

    public void callNearbySearchByType(String location, String type, OnResponseListener<NearbySearchResponse> listener) {
        Call<NearbySearchResponse> callDetails = repository.getRetrofitService().getNearbyByType(5000, location, type);
        callDetails.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<NearbySearchResponse> call, @NotNull Response<NearbySearchResponse> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<NearbySearchResponse> call, @NotNull Throwable t) {

            }
        });
    }

    public void callNearbySearchByKeyword(String location, String keyword, OnResponseListener<NearbySearchResponse> listener) {
        Call<NearbySearchResponse> callDetails = repository.getRetrofitService().getNearbyByKeyword(5000, location, keyword);
        callDetails.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<NearbySearchResponse> call, @NotNull Response<NearbySearchResponse> response) {
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<NearbySearchResponse> call, @NotNull Throwable t) {

            }
        });
    }

    // ---------------
    // Firebase
    // ---------------
    public FirebaseUser getCurrentFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void callToggleEatingAt(String placeID, String placeName, OnToggledListener toggledEatingAt) {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = mCurrentUser;
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (document.getId().equals(user.getUid())) {
                                String eatingAt = (String) document.get("eatingAt");
                                String eatingAtName;

                                boolean isIn = false;
                                if (eatingAt != null && eatingAt.equals(placeID)) {
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
                                        .addOnSuccessListener(aVoid -> toggledEatingAt.onToggled(!finalWasIn));
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void callToggleLike(String placeID, OnToggledListener toggledLike) {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = mCurrentUser;
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (document.getId().equals(user.getUid())) {
                                List<String> likesList = (List<String>) document.get("likes");
                                boolean wasIn = false;
                                if (likesList != null) {
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
                                }
                                boolean finalWasIn = wasIn;
                                mFirebaseFirestore.collection("users")
                                        .document(user.getUid())
                                        .update(
                                                "likes", likesList
                                        )
                                        .addOnSuccessListener(aVoid -> toggledLike.onToggled(!finalWasIn));
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void callUserList(OnUserListListener listener) {
        mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> userList = mUserMutableLiveDat;
                        userList.clear();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (document != null) {
                                Object likes = document.get("likes");
                                List<String> likesList;
                                if (likes instanceof List) {
                                    likesList = (List<String>) likes;
                                } else {
                                    likesList = new ArrayList<>();
                                }
                                String newUserUID = document.getId();
                                User newUser = new User(
                                        newUserUID,
                                        (String) document.get("name"),
                                        likesList,
                                        (String) document.get("avatarUrl"),
                                        (String) document.get("eatingAt"),
                                        (String) document.get("eatingAtName"),
                                        (String) document.get("email")
                                );
                                userList.add(newUser);

                                if (newUserUID.equals(getCurrentFirebaseUser().getUid())) {
                                    mCurrentUser = newUser;
                                }
                            }
                        }
                        listener.onResponse(mCurrentUser, mUserMutableLiveDat);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void callCurrentUser(OnResponseListener<User> listener) {
        if (getCurrentFirebaseUser() != null) {
            mFirebaseFirestore.collection("users")
                    .document(getCurrentFirebaseUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                Object likes = document.get("likes");
                                List<String> likesList;
                                if (likes instanceof List) {
                                    likesList = (List<String>) likes;
                                } else {
                                    likesList = new ArrayList<>();
                                }
                                String newUserUID = document.getId();
                                User newUser = new User(
                                        newUserUID,
                                        (String) document.get("name"),
                                        likesList,
                                        (String) document.get("avatarUrl"),
                                        (String) document.get("eatingAt"),
                                        (String) document.get("eatingAtName"),
                                        (String) document.get("email")
                                );
                                listener.onResponse(newUser);
                            }
                        }
                    });
        } else {
            listener.onResponse(new User(
                    "",
                    "Not Connected",
                    new ArrayList<>(),
                    null,
                    "",
                    "",
                    ""
            ));
        }
    }

    public Boolean isCurrentUserLogged() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void updateUserOnFirebase() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        FirebaseUser user = getCurrentFirebaseUser();
        if (user != null) {
            DocumentReference docRef = rootRef.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> userToUpdate = new HashMap<>();
                    userToUpdate.put("name", user.getDisplayName());
                    userToUpdate.put("email", user.getEmail());
                    Uri photoUrl = user.getPhotoUrl();
                    String photoUrlString;
                    if (photoUrl == null)
                        photoUrlString = "";
                    else
                        photoUrlString = photoUrl.toString();
                    userToUpdate.put("avatarUrl", photoUrlString);

                    if (!Objects.requireNonNull(document).exists()) {
                        userToUpdate.put("likes", new ArrayList<String>());
                        userToUpdate.put("eatingAt", "");
                        userToUpdate.put("eatingAtName", "");
                    } else {
                        userToUpdate.put("likes", document.get("likes"));
                        userToUpdate.put("eatingAt", document.get("eatingAt"));
                        userToUpdate.put("eatingAtName", document.get("eatingAtName"));
                    }

                    rootRef.collection("users").document(user.getUid())
                            .set(userToUpdate)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "User DocumentSnapshot successfully written!"))
                            .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
                } else {
                    Log.d(TAG, "Error getting Document with ", task.getException());
                }
            });
        }
    }
}
