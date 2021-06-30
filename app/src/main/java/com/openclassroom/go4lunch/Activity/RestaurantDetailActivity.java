package com.openclassroom.go4lunch.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.openclassroom.go4lunch.Model.User;
import com.openclassroom.go4lunch.View.RecyclerView.ParticipantListAdapter;
import com.openclassroom.go4lunch.databinding.ActivityRestaurantDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class RestaurantDetailActivity extends AppCompatActivity {

    ActivityRestaurantDetailBinding mBinding;

    private final ParticipantListAdapter mParticipantListAdapter = new ParticipantListAdapter(new ArrayList<User>(
            Arrays.asList(
                    new User(),
                    new User(),
                    new User()
            )
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.participantList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBinding.participantList.setAdapter(mParticipantListAdapter);

    }
}