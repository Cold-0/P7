package com.openclassroom.go4lunch.View.Fragment;

import com.openclassroom.go4lunch.Model.User;
import com.openclassroom.go4lunch.View.Fragment.Abstract.FragmentX;
import com.openclassroom.go4lunch.View.RecyclerView.WorkmatesListAdapter;
import com.openclassroom.go4lunch.ViewModel.WorkmatesViewModel;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends FragmentX {

    private FragmentWorkmatesBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WorkmatesViewModel workmatesListViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        mBinding = FragmentWorkmatesBinding.inflate(inflater, container, false);

        WorkmatesListAdapter listViewAdapter = new WorkmatesListAdapter(new MutableLiveData<List<User>>(new ArrayList<User>()));
        mBinding.workmatesList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.workmatesList.setAdapter(listViewAdapter);

        final Observer<List<User>> nameObserver = v -> {
            listViewAdapter.notifyDataSetChanged();
        };

        workmatesListViewModel.getUserList().observe(getViewLifecycleOwner(), nameObserver);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}