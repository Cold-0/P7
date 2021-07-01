package com.openclassroom.go4lunch.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.View.RecyclerView.RestaurantsListAdapter;
import com.openclassroom.go4lunch.ViewModel.RestaurantListViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

public class RestaurantsListFragment extends Fragment {

    private FragmentListviewBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RestaurantListViewModel restaurantListViewModel = new ViewModelProvider(this).get(RestaurantListViewModel.class);
        mBinding = FragmentListviewBinding.inflate(inflater, container, false);

        RestaurantsListAdapter listViewAdapter = new RestaurantsListAdapter(restaurantListViewModel.getRestaurantList());
        mBinding.restaurantList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.restaurantList.setAdapter(listViewAdapter);

        return mBinding.getRoot();
    }

    

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}