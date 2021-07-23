package com.openclassroom.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.message.RestaurantAddMessage;
import com.openclassroom.go4lunch.message.SearchValidateMessage;
import com.openclassroom.go4lunch.type.SearchType;
import com.openclassroom.go4lunch.utils.ex.FragmentEX;
import com.openclassroom.go4lunch.type.FragmentViewType;
import com.openclassroom.go4lunch.view.recyclerview.RestaurantsListAdapter;
import com.openclassroom.go4lunch.viewmodel.SearchViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;
import com.openclassroom.go4lunch.viewmodel.UserInfoViewModel;

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

        searchViewModel.getClearRestaurantList().addObserver((o, arg) -> {
            mRestaurantsListAdapter.clearRestaurantList();
        });

        SearchValidateMessage svd = new SearchValidateMessage()
                .searchmethod(SearchType.CLOSER)
                .viewtype(FragmentViewType.LIST);

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