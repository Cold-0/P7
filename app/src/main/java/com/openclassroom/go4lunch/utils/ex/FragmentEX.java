package com.openclassroom.go4lunch.utils.ex;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.openclassroom.go4lunch.view.activity.RestaurantDetailActivity;

public abstract class FragmentEX extends Fragment {

    protected void openDetailRestaurant(Activity activity, String placeID) {
        Intent sendStuff = new Intent(activity, RestaurantDetailActivity.class);
        sendStuff.putExtra("placeID", placeID);
        startActivity(sendStuff);
    }

    public FragmentEX() {

    }
}
