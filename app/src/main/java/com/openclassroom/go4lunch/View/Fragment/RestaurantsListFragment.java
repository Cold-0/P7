package com.openclassroom.go4lunch.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.Result;
import com.openclassroom.go4lunch.View.Fragment.Abstract.FragmentX;
import com.openclassroom.go4lunch.View.RecyclerView.RestaurantsListAdapter;
import com.openclassroom.go4lunch.ViewModel.SearchViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

import java.util.ArrayList;

public class RestaurantsListFragment extends FragmentX {

    private FragmentListviewBinding mBinding;
    private RestaurantsListAdapter mRestaurantsListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SearchViewModel searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mBinding = FragmentListviewBinding.inflate(inflater, container, false);

        mRestaurantsListAdapter = new RestaurantsListAdapter(requireActivity(), new ArrayList<>());
        mBinding.restaurantList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.restaurantList.setAdapter(mRestaurantsListAdapter);

        searchViewModel.getAddRestaurantToList().addObserver((o, arg) -> {
            if (arg instanceof Result)
                mRestaurantsListAdapter.addToRestaurantList((Result) arg);
        });

        searchViewModel.getClearRestaurantList().addObserver((o, arg) -> {
            mRestaurantsListAdapter.clearRestaurantList();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}