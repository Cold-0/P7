package com.openclassroom.go4lunch.View.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.Model.User;
import com.openclassroom.go4lunch.databinding.ItemUserBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListViewHolder> {

    @NonNull
    private LiveData<List<User>> mUserList;

    public WorkmatesListAdapter(@NonNull LiveData<List<User>> userList) {
        mUserList = userList;
    }

    @NonNull
    @NotNull
    @Override
    public WorkmatesListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkmatesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mUserList.getValue().size();
    }

}