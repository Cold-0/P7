package com.openclassroom.go4lunch.View.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.Model.User;
import com.openclassroom.go4lunch.databinding.ItemUserBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListViewHolder> {

    @NonNull
    private List<User> mParticipantList;

    public ParticipantListAdapter(@NonNull List<User> participantList) {
        mParticipantList = participantList;
    }

    @NonNull
    @NotNull
    @Override
    public ParticipantListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ParticipantListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ParticipantListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mParticipantList.size();
    }

}