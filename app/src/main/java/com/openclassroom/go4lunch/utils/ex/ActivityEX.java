package com.openclassroom.go4lunch.utils.ex;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.repository.Repository;

public abstract class ActivityEX extends AppCompatActivity {

    // --------------------
    // UTILS
    // --------------------
    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public ActivityEX() {
        mRepository = Repository.getRepository();
    }
}