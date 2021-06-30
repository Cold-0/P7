package com.openclassroom.go4lunch.ViewHolder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.Model.Restaurant;
import com.openclassroom.go4lunch.databinding.ItemRestaurantBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListViewHolder> {

    @NonNull
    private List<Restaurant> mRestaurantList;

    public RestaurantsListAdapter(@NonNull List<Restaurant> restaurantList) {
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
        return mRestaurantList.size();
    }

}