package com.openclassroom.go4lunch.View.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.Result;
import com.openclassroom.go4lunch.databinding.ItemRestaurantBinding;

class RestaurantsListViewHolder extends RecyclerView.ViewHolder {
    public final ItemRestaurantBinding mBinding;

    RestaurantsListViewHolder(@NonNull ItemRestaurantBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    void bind() {

    }
}
