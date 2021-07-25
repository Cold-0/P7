package com.openclassroom.go4lunch.utils.ex;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassroom.go4lunch.views.activity.RestaurantDetailActivity;

public abstract class ActivityEX extends AppCompatActivity {
    // ----------------------------
    // Intent
    // ----------------------------
    protected void openDetailRestaurant(String placeID) {
        Intent sendStuff = new Intent(this, RestaurantDetailActivity.class);
        sendStuff.putExtra("placeID", placeID);
        startActivity(sendStuff);
    }
}