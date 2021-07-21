package com.openclassroom.go4lunch.view.recyclerview;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.databinding.ItemUserBinding;
import com.openclassroom.go4lunch.model.nearbysearchapi.NearbySearchResult;
import com.openclassroom.go4lunch.utils.transform.CircleCropTransform;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListViewHolder> {

    @NonNull
    private List<User> mParticipantList;

    @NonNull
    private final FragmentActivity mActivity;

    public ParticipantListAdapter(@NonNull List<User> participantList, @NonNull FragmentActivity activity) {
        mParticipantList = participantList;
        mActivity = activity;
    }

    public void clearUserList() {
        mParticipantList.clear();
        notifyDataSetChanged();
    }

    public void addUserList(User user) {
        mParticipantList.add(user);
        notifyItemInserted(mParticipantList.size() - 1);
    }

    @NonNull
    @NotNull
    @Override
    public ParticipantListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ParticipantListViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ParticipantListViewHolder holder, int position) {
        User user = Objects.requireNonNull(mParticipantList).get(position);

        if (user.getEatingAt().equals("")) {
            holder.mBinding.userInfo.setText(user.getDisplayName() + mActivity.getString(R.string.didnt_decided_yet));
            holder.mBinding.userInfo.setTypeface(null, Typeface.ITALIC);
            holder.mBinding.userInfo.setTextColor(mActivity.getResources().getColor(R.color.lightgrey));
        } else {
            holder.mBinding.userInfo.setText(user.getDisplayName() + mActivity.getString(R.string.is_eating_at));
            holder.mBinding.userInfo.setTypeface(null, Typeface.NORMAL);
            holder.mBinding.userInfo.setTextColor(mActivity.getResources().getColor(R.color.black));
        }

        if (!user.getAvatarUrl().equals("") && user.getAvatarUrl() != null) {
            Picasso.Builder builder = new Picasso.Builder(mActivity);
            builder.downloader(new OkHttp3Downloader(mActivity));
            builder.build().load(user.getAvatarUrl())
                    .error(R.drawable.ic_launcher_foreground)
                    .transform(new CircleCropTransform())
                    .into(holder.mBinding.userAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return mParticipantList.size();
    }

}