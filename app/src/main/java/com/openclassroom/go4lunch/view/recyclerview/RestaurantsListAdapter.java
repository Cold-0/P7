package com.openclassroom.go4lunch.view.recyclerview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.BuildConfig;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.data.RestaurantInfoData;
import com.openclassroom.go4lunch.model.nearbysearchapi.NearbySearchResult;
import com.openclassroom.go4lunch.model.placedetailsapi.DetailsResult;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.databinding.ItemRestaurantBinding;
import com.openclassroom.go4lunch.view.activity.RestaurantDetailActivity;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsListViewHolder> {

    private static final String TAG = RestaurantsListAdapter.class.toString();

    @NonNull
    private final FragmentActivity mActivity;

    @NonNull
    private final LiveData<List<RestaurantInfoData>> mRestaurantList;

    @NonNull
    public LiveData<List<RestaurantInfoData>> getRestaurantList() {
        return mRestaurantList;
    }

    public void clearRestaurantList() {
        Objects.requireNonNull(mRestaurantList.getValue()).clear();
        Log.w(TAG, "clearRestaurantList");
        notifyDataSetChanged();
    }

    public void addToRestaurantList(RestaurantInfoData restaurant) {
        Objects.requireNonNull(mRestaurantList.getValue()).add(restaurant);
        notifyItemInserted(mRestaurantList.getValue().size() - 1);
        Log.w(TAG, "addToRestaurantList");
    }

    public RestaurantsListAdapter(@NonNull FragmentActivity activity, @NonNull List<RestaurantInfoData> restaurantList) {
        mActivity = activity;
        mRestaurantList = new MutableLiveData<>(restaurantList);
    }

    @NonNull
    @NotNull
    @Override
    public RestaurantsListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        @NonNull ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RestaurantsListViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantsListViewHolder holder, int position) {
        NearbySearchResult restaurant = Objects.requireNonNull(mRestaurantList.getValue()).get(position).result;
        List<User> userList = Objects.requireNonNull(mRestaurantList.getValue()).get(position).userList;

        Location loc = getMyLocation();

        holder.mBinding.restaurantName.setText(restaurant.getName());

        Location to = new Location("");
        to.setLatitude(restaurant.getGeometry().getLocation().getLat());
        to.setLongitude(restaurant.getGeometry().getLocation().getLng());

        holder.mBinding.distance.setText((int) (loc.distanceTo(to)) + "m");
        if (restaurant.getRating() != null)
            holder.mBinding.ratingBar.setRating(restaurant.getRating().floatValue() / 5.0f * 3.0f);

        holder.mBinding.typeAndAddress.setText(restaurant.getVicinity());

        holder.mBinding.getRoot().setOnClickListener(v -> {
            Intent sendStuff = new Intent(mActivity, RestaurantDetailActivity.class);
            sendStuff.putExtra("placeID", restaurant.getPlaceId());
            mActivity.startActivity(sendStuff);
        });

        holder.mBinding.workmatesIcon.setVisibility(View.INVISIBLE);
        holder.mBinding.workmatesNumber.setVisibility(View.INVISIBLE);

        int numberEatingAt = 0;

        for (User user : userList) {
            if (user.getEatingAt().equals(restaurant.getPlaceId())) {
                numberEatingAt++;
            }
        }

        if (numberEatingAt > 0) {
            holder.mBinding.workmatesIcon.setVisibility(View.VISIBLE);
            holder.mBinding.workmatesNumber.setVisibility(View.VISIBLE);
            holder.mBinding.workmatesNumber.setText("(" + numberEatingAt + ")");
        }

        if (restaurant.getOpeningHours() != null && restaurant.getOpeningHours().getOpenNow()) {
            holder.mBinding.openHours.setText("Currently Open");

            holder.mBinding.openHours.setTextColor(mActivity.getResources().getColor(R.color.green));
        } else {
            holder.mBinding.openHours.setText("Currently Closed");
            holder.mBinding.openHours.setTextColor(mActivity.getResources().getColor(R.color.red));
        }

        if (restaurant.getPhotos() != null) {
            String photo_reference = restaurant.getPhotos().get(0).getPhotoReference();
            if (!photo_reference.equals("")) {
                Picasso.Builder builder = new Picasso.Builder(mActivity);
                builder.downloader(new OkHttp3Downloader(mActivity));
                builder.build().load("https://maps.googleapis.com/maps/api/place/photo?parameters&key=" + BuildConfig.MAPS_API_KEY + "&photoreference=" + photo_reference + "&maxwidth=512&maxheight=512")
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.mBinding.imagePreview);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(mRestaurantList.getValue()).size();
    }

    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((mActivity), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location loc = new Location("");
            loc.setLatitude(0.0);
            loc.setLongitude(0.0);
            return loc;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    static class RestaurantsListViewHolder extends RecyclerView.ViewHolder {
        public final ItemRestaurantBinding mBinding;

        RestaurantsListViewHolder(@NonNull ItemRestaurantBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind() {

        }
    }
}