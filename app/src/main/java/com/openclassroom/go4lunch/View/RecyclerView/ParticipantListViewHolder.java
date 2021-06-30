package com.openclassroom.go4lunch.View.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.databinding.ItemUserBinding;

class ParticipantListViewHolder extends RecyclerView.ViewHolder {
    private final ItemUserBinding mBinding;

    ParticipantListViewHolder(@NonNull ItemUserBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    void bind() {

    }
}
