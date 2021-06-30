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
import com.openclassroom.go4lunch.Model.Workmate;
import com.openclassroom.go4lunch.ViewHolder.RestaurantsListAdapter;
import com.openclassroom.go4lunch.ViewHolder.WorkmatesListAdapter;
import com.openclassroom.go4lunch.ViewModel.WorkmatesViewModel;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel mWorkmatesViewModel;
    private FragmentWorkmatesBinding mBinding;

    private final WorkmatesListAdapter mWorkmatesListAdapter = new WorkmatesListAdapter(new ArrayList<Workmate>(
            Arrays.asList(
                    new Workmate(),
                    new Workmate(),
                    new Workmate(),
                    new Workmate()
            )
    ));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mWorkmatesViewModel =
                new ViewModelProvider(this).get(WorkmatesViewModel.class);

        mBinding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        mBinding.workmatesList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.workmatesList.setAdapter(mWorkmatesListAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}