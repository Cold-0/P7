package com.openclassroom.go4lunch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassroom.go4lunch.Model.User;
import com.openclassroom.go4lunch.View.RecyclerView.WorkmatesListAdapter;
import com.openclassroom.go4lunch.ViewModel.WorkmatesViewModel;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel mWorkmatesViewModel;
    private FragmentWorkmatesBinding mBinding;

    private final WorkmatesListAdapter mWorkmatesListAdapter = new WorkmatesListAdapter(new ArrayList<User>(
            Arrays.asList(
                    new User(),
                    new User(),
                    new User(),
                    new User()
            )
    ));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mWorkmatesViewModel =
                new ViewModelProvider(this).get(WorkmatesViewModel.class);

        mBinding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation());

        mBinding.workmatesList.setLayoutManager(linearLayoutManager);
        mBinding.workmatesList.setAdapter(mWorkmatesListAdapter);
        mBinding.workmatesList.addItemDecoration(dividerItemDecoration);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}