package com.openclassroom.go4lunch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.Model.Restaurant;
import com.openclassroom.go4lunch.View.RecyclerView.RestaurantsListAdapter;
import com.openclassroom.go4lunch.ViewModel.RestaurantListViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class RestaurantsListFragment extends Fragment {

    private RestaurantListViewModel mRestaurantListViewModel;
    private FragmentListviewBinding mBinding;

    private final RestaurantsListAdapter mListViewAdapter = new RestaurantsListAdapter(new ArrayList<Restaurant>(
            Arrays.asList(
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant(),
                    new Restaurant()
            )
    ));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRestaurantListViewModel =
                new ViewModelProvider(this).get(RestaurantListViewModel.class);

        mBinding = FragmentListviewBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        mBinding.restaurantList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.restaurantList.setAdapter(mListViewAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}