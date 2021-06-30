package com.openclassroom.go4lunch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassroom.go4lunch.ViewModel.MapViewViewModel;
import com.openclassroom.go4lunch.databinding.FragmentMapviewBinding;

import org.jetbrains.annotations.NotNull;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private MapViewViewModel mMapViewViewModel;
    private FragmentMapviewBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMapViewViewModel = new ViewModelProvider(this).get(MapViewViewModel.class);
        mBinding = FragmentMapviewBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
}