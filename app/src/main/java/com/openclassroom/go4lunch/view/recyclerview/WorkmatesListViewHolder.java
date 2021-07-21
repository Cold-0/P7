package com.openclassroom.go4lunch.view.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.databinding.ItemUserBinding;

class WorkmatesListViewHolder extends RecyclerView.ViewHolder {
    public final ItemUserBinding mBinding;

    WorkmatesListViewHolder(@NonNull ItemUserBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    void bind() {

    }
}
