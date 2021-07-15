package com.openclassroom.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.view.recyclerview.ParticipantListAdapter;
import com.openclassroom.go4lunch.databinding.ActivityRestaurantDetailBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class RestaurantDetailActivity extends AppCompatActivity {

    ActivityRestaurantDetailBinding mBinding;

    private final ParticipantListAdapter mParticipantListAdapter = new ParticipantListAdapter(new ArrayList<User>(
            Arrays.asList(
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512"),
                    new User(1, "Bob", new ArrayList<String>(), "https://i.pravatar.cc/512")
            )
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        configureRecyclerView();
        configureButtons();
    }

    private void configureButtons() {
        mBinding.returnButton.setOnClickListener(v -> {
            finish();
        });

        mBinding.layoutClickableCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789"));
            startActivity(intent);
        });

        mBinding.layoutClickableLike.setOnClickListener(v -> {

        });

        mBinding.layoutClickableWebsite.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        });
    }

    private void configureRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());

        mBinding.participantList.setLayoutManager(linearLayoutManager);
        mBinding.participantList.setAdapter(mParticipantListAdapter);
        mBinding.participantList.addItemDecoration(dividerItemDecoration);
    }
}