package com.openclassroom.go4lunch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.openclassroom.go4lunch.ViewModel.ListViewViewModel;
import com.openclassroom.go4lunch.databinding.FragmentListviewBinding;

public class ListViewFragment extends Fragment {

    private ListViewViewModel mListViewViewModel;
    private FragmentListviewBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListViewViewModel =
                new ViewModelProvider(this).get(ListViewViewModel.class);

        mBinding = FragmentListviewBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        final TextView textView = mBinding.textGallery;
        mListViewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}