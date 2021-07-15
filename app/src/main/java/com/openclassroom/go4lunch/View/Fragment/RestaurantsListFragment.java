package com.openclassroom.go4lunch.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.Result;
import com.openclassroom.go4lunch.Repository.Repository;
import com.openclassroom.go4lunch.View.RecyclerView.RestaurantsListAdapter;
import com.openclassroom.go4lunch.ViewModel.SearchViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class RestaurantsListFragment extends Fragment {

    private FragmentListviewBinding mBinding;
    private Repository mRepository;
    private RestaurantsListAdapter mRestaurantsListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SearchViewModel searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mBinding = FragmentListviewBinding.inflate(inflater, container, false);

        mRepository = Repository.getRepository();

        mRestaurantsListAdapter = new RestaurantsListAdapter(requireActivity(), new ArrayList<Result>());
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