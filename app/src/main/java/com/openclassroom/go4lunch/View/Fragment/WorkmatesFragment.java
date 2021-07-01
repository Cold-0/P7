package com.openclassroom.go4lunch.View.Fragment;

import com.openclassroom.go4lunch.View.RecyclerView.WorkmatesListAdapter;
import com.openclassroom.go4lunch.ViewModel.WorkmatesViewModel;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WorkmatesViewModel workmatesListViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);
        mBinding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        WorkmatesListAdapter listViewAdapter = new WorkmatesListAdapter(workmatesListViewModel.getUserList());
        mBinding.workmatesList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.workmatesList.setAdapter(listViewAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}