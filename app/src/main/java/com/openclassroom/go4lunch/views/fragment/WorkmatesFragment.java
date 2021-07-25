package com.openclassroom.go4lunch.views.fragment;

import com.openclassroom.go4lunch.models.User;
import com.openclassroom.go4lunch.utils.ex.FragmentEX;
import com.openclassroom.go4lunch.views.recyclerview.WorkmatesListAdapter;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;
import com.openclassroom.go4lunch.viewmodels.UserInfoViewModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class WorkmatesFragment extends FragmentEX {
    // ------------------------
    // Properties
    // ------------------------
    private FragmentWorkmatesBinding mBinding;

    // ------------------------
    // " Constructor "
    // ------------------------
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        UserInfoViewModel workmatesListViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mBinding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        WorkmatesListAdapter listViewAdapter = new WorkmatesListAdapter(requireActivity(), new ArrayList<>());
        mBinding.workmatesList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.workmatesList.setAdapter(listViewAdapter);

        workmatesListViewModel.callUserList((currentUser, userList) -> {
            listViewAdapter.clearUserList();
            for (User user : userList) {
                if (!user.getUid().equals(currentUser.getUid())) {
                    listViewAdapter.addUserList(user);
                }
            }
        });

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