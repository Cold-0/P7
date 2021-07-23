package com.openclassroom.go4lunch.view.fragment;

import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.utils.ex.FragmentEX;
import com.openclassroom.go4lunch.view.recyclerview.WorkmatesListAdapter;
import com.openclassroom.go4lunch.viewmodel.UserInfoViewModel;
import com.openclassroom.go4lunch.viewmodel.WorkmatesViewModel;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmatesFragment extends FragmentEX {

    private FragmentWorkmatesBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WorkmatesViewModel workmatesListViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        UserInfoViewModel userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mBinding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        WorkmatesListAdapter listViewAdapter = new WorkmatesListAdapter(requireActivity(), new ArrayList<>(Objects.requireNonNull(workmatesListViewModel.getUserList().getValue())));
        mBinding.workmatesList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.workmatesList.setAdapter(listViewAdapter);

        // Observe user list and reload the Adapter with new data.
        workmatesListViewModel.getUserList().observe(getViewLifecycleOwner(), users -> {
            listViewAdapter.clearUserList();
            for (User user : users) {
                if (!user.getUid().equals(Objects.requireNonNull(userInfoViewModel.getCurrentUser().getValue()).getUid())) {
                    listViewAdapter.addUserList(user);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}