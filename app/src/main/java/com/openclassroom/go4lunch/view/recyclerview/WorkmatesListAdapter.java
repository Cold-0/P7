package com.openclassroom.go4lunch.view.recyclerview;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.databinding.ItemUserBinding;
import com.openclassroom.go4lunch.utils.transform.CircleCropTransform;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListAdapter.WorkmatesListViewHolder> {

    @NonNull
    private final List<User> mUserList;

    @NonNull
    private final FragmentActivity mActivity;

    public WorkmatesListAdapter(@NonNull FragmentActivity activity, @NonNull List<User> userList) {
        mActivity = activity;
        mUserList = userList;
    }

    public void clearUserList() {
        mUserList.clear();
        notifyDataSetChanged();
    }

    public void addUserList(User user) {
        mUserList.add(user);
        notifyItemInserted(mUserList.size() - 1);
    }

    @NonNull
    @NotNull
    @Override
    public WorkmatesListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmatesListViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkmatesListViewHolder holder, int position) {
        User user = Objects.requireNonNull(mUserList).get(position);

        if (user.getEatingAt().equals("")) {
            holder.mBinding.userInfo.setText(user.getDisplayName() + mActivity.getString(R.string.didnt_decided_yet));
            holder.mBinding.userInfo.setTypeface(null, Typeface.ITALIC);
            holder.mBinding.userInfo.setTextColor(mActivity.getResources().getColor(R.color.lightgrey));
        } else {
            holder.mBinding.userInfo.setText(user.getDisplayName() + mActivity.getString(R.string.is_eating_at) + user.getEatingAtName());
            holder.mBinding.userInfo.setTypeface(null, Typeface.NORMAL);
            holder.mBinding.userInfo.setTextColor(mActivity.getResources().getColor(R.color.black));
        }

        if (!user.getPhotoUrl().equals("") && user.getPhotoUrl() != null) {
            Picasso.Builder builder = new Picasso.Builder(mActivity);
            builder.downloader(new OkHttp3Downloader(mActivity));
            builder.build().load(user.getPhotoUrl())
                    .error(R.drawable.ic_launcher_foreground)
                    .transform(new CircleCropTransform())
                    .into(holder.mBinding.userAvatar);
        } else {
            holder.mBinding.userAvatar.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mUserList).size();
    }

    static class WorkmatesListViewHolder extends RecyclerView.ViewHolder {
        public final ItemUserBinding mBinding;

        WorkmatesListViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

}