package com.openclassroom.go4lunch.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.messages.RestaurantAddMessage;
import com.openclassroom.go4lunch.messages.SearchValidateMessage;
import com.openclassroom.go4lunch.types.SearchType;
import com.openclassroom.go4lunch.utils.ex.FragmentEX;
import com.openclassroom.go4lunch.views.recyclerview.RestaurantsListAdapter;
import com.openclassroom.go4lunch.viewmodels.SearchViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

import java.util.ArrayList;

public class RestaurantsListFragment extends FragmentEX {

    // ------------------------
    // Properties
    // ------------------------
    private FragmentListviewBinding mBinding;
    private RestaurantsListAdapter mRestaurantsListAdapter;

    // ------------------------
    // " Constructor "
    // ------------------------
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mBinding = FragmentListviewBinding.inflate(inflater, container, false);

        mRestaurantsListAdapter = new RestaurantsListAdapter(requireActivity(), new ArrayList<>());
        mBinding.restaurantList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.restaurantList.setAdapter(mRestaurantsListAdapter);

        searchViewModel.getAddRestaurantToList().addObserver((o, arg) -> {
            if (arg instanceof RestaurantAddMessage)
                mRestaurantsListAdapter.addToRestaurantList((RestaurantAddMessage) arg);
        });

        searchViewModel.getClearRestaurantList().addObserver((o, arg) -> mRestaurantsListAdapter.clearRestaurantList());

        SearchValidateMessage svd = new SearchValidateMessage()
                .setSearchMethod(SearchType.CLOSER);

        searchViewModel.setSearchValidationDataViewMutable(svd);

        return mBinding.getRoot();
    }

    // ------------------------
    // Override
    // ------------------------
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}