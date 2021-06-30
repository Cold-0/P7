package com.openclassroom.go4lunch.ViewHolder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.Model.Restaurant;
import com.openclassroom.go4lunch.Model.Workmate;
import com.openclassroom.go4lunch.databinding.ItemWorkmatesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListViewHolder> {

    @NonNull
    private List<Workmate> mWorkmatesList;

    public WorkmatesListAdapter(@NonNull List<Workmate> workmateList) {
        mWorkmatesList = workmateList;
    }

    @NonNull
    @NotNull
    @Override
    public WorkmatesListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemWorkmatesBinding binding = ItemWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkmatesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mWorkmatesList.size();
    }

}