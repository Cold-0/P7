package com.openclassroom.go4lunch.view.recyclerview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.go4lunch.BuildConfig;
import com.openclassroom.go4lunch.model.placedetailsapi.Result;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.databinding.ItemRestaurantBinding;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListViewHolder> {

    private static final String TAG = RestaurantsListAdapter.class.toString();

    @NonNull
    private final FragmentActivity mActivity;

    @NonNull
    private final LiveData<List<Result>> mRestaurantList;

    @NonNull
    public LiveData<List<Result>> getRestaurantList() {
        return mRestaurantList;
    }

    public void clearRestaurantList() {
        Objects.requireNonNull(mRestaurantList.getValue()).clear();
        Log.w(TAG, "clearRestaurantList");
        notifyDataSetChanged();
    }

    public void addToRestaurantList(Result restaurant) {
        Objects.requireNonNull(mRestaurantList.getValue()).add(restaurant);
        notifyItemInserted(mRestaurantList.getValue().size() - 1);
        Log.w(TAG, "addToRestaurantList");
    }

    public RestaurantsListAdapter(@NonNull FragmentActivity activity, @NonNull List<Result> restaurantList) {
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
        Result restaurant = mRestaurantList.getValue().get(position);
        Location loc = getMyLocation();

        holder.mBinding.restaurantName.setText(restaurant.getName());
        holder.mBinding.workmatesNumber.setText("(5)");

        Location to = new Location("");
        to.setLatitude(restaurant.getGeometry().getLocation().getLat());
        to.setLongitude(restaurant.getGeometry().getLocation().getLng());

        holder.mBinding.distance.setText((int) (loc.distanceTo(to)) + "m");
        if (restaurant.getRating() != null)
            holder.mBinding.ratingBar.setRating((float) restaurant.getRating().doubleValue());

        holder.mBinding.typeAndAddress.setText(restaurant.getFormattedAddress());

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

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return dist;
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
}