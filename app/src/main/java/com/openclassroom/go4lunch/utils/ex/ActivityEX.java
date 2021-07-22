package com.openclassroom.go4lunch.utils.ex;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.view.activity.RestaurantDetailActivity;

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

    protected void openDetailRestaurant(String placeID) {
        Intent sendStuff = new Intent(this, RestaurantDetailActivity.class);
        sendStuff.putExtra("placeID", placeID);
        startActivity(sendStuff);
    }

    public ActivityEX() {

    }
}