package com.openclassroom.go4lunch.View.Fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.Model.NearbySearchAPI.NearbySearchResponse;
import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.PlaceDetailsResponse;
import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.Result;
import com.openclassroom.go4lunch.Repository.Repository;
import com.openclassroom.go4lunch.View.RecyclerView.RestaurantsListAdapter;
import com.openclassroom.go4lunch.ViewModel.RestaurantListViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsListFragment extends Fragment {

    private FragmentListviewBinding mBinding;
    private Repository mRepository;
    private RestaurantsListAdapter mRestaurantsListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RestaurantListViewModel restaurantListViewModel = new ViewModelProvider(requireActivity()).get(RestaurantListViewModel.class);
        mBinding = FragmentListviewBinding.inflate(inflater, container, false);

        mRepository = Repository.getRepository();

        mRestaurantsListAdapter = new RestaurantsListAdapter(requireActivity(), new ArrayList<Result>());
        mBinding.restaurantList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.restaurantList.setAdapter(mRestaurantsListAdapter);

        restaurantListViewModel.getRestaurantList().observe(requireActivity(), this::OnPredictionSelected);

        return mBinding.getRoot();
    }

    // Callback when prediction is selected
    private void OnPredictionSelected(@NotNull Result result) {
        Location loc = new Location("");
        Double lat = result.getGeometry().getLocation().getLat();
        Double lng = result.getGeometry().getLocation().getLng();
        loc.setLatitude(lat);
        loc.setLongitude(lng);

        // Get Nearby Search Respond using the Details as location
        @SuppressLint("DefaultLocale") Call<NearbySearchResponse> callNearbySearch = mRepository.getService().getNearbyByType(10000, String.format("%f,%f", lat, lng), "restaurant");
        callNearbySearch.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                OnNearbySearchResponseFromDetailResponse(response);
            }

            @Override
            public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
            }
        });
    }

    // Callback when nearby search from detail position received
    private void OnNearbySearchResponseFromDetailResponse(@NotNull Response<NearbySearchResponse> response) {
        assert response.body() != null;
        mRestaurantsListAdapter.clearRestaurantList();
        for (com.openclassroom.go4lunch.Model.NearbySearchAPI.Result result : response.body().getResults()) {
            @SuppressLint("DefaultLocale") Call<PlaceDetailsResponse> callDetails = mRepository.getService().getDetails(result.getPlaceId());
            callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                    OnDetailResponsePerEachNearbySearchResult(response);
                }

                @Override
                public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {

                }
            });
        }
    }

    // Callback for Details of each result of the nearby search
    private void OnDetailResponsePerEachNearbySearchResult(@NotNull Response<PlaceDetailsResponse> response) {
        assert response.body() != null;
        mRestaurantsListAdapter.addToRestaurantList(response.body().getResult());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}