package com.openclassroom.go4lunch.View.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.Model.Restaurant;
import com.openclassroom.go4lunch.databinding.ItemRestaurantBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListViewHolder> {

    @NonNull
    private LiveData<List<Restaurant>> mRestaurantList;

    public RestaurantsListAdapter(@NonNull LiveData<List<Restaurant>> restaurantList) {
        mRestaurantList = restaurantList;
    }

    @NonNull
    @NotNull
    @Override
    public RestaurantsListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RestaurantsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantsListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mRestaurantList.getValue()).size();
    }

}