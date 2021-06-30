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

import com.openclassroom.go4lunch.ViewModel.WorkmatersViewModel;
import com.openclassroom.go4lunch.databinding.FragmentWorkmatesBinding;

public class WorkmatesFragment extends Fragment {

    private WorkmatersViewModel mWorkmatersViewModel;
    private FragmentWorkmatesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mWorkmatersViewModel =
                new ViewModelProvider(this).get(WorkmatersViewModel.class);

        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        mWorkmatersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        binding = null;
    }
}