package com.openclassroom.go4lunch.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.message.SearchValidateMessage;
import com.openclassroom.go4lunch.type.SearchType;
import com.openclassroom.go4lunch.utils.ex.FragmentEX;
import com.openclassroom.go4lunch.type.FragmentViewType;
import com.openclassroom.go4lunch.message.MarkerAddMessage;
import com.openclassroom.go4lunch.viewmodel.SearchViewModel;
import com.openclassroom.go4lunch.databinding.FragmentMapviewBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapViewFragment extends FragmentEX implements OnMapReadyCallback {
    // ------------------------
    // Properties
    // ------------------------
    private FragmentMapviewBinding mBinding;
    private GoogleMap mGoogleMap;
    private SearchViewModel mSearchViewModel;
    private final Map<Marker, String> mMarkerStringHashMap = new HashMap<>();

    // ------------------------
    // " Constructor "
    // ------------------------
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentMapviewBinding.inflate(inflater, container, false);
        mBinding.mapView.onCreate(savedInstanceState);

        mSearchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        MapsInitializer.initialize(requireActivity().getApplicationContext());

        mBinding.mapView.getMapAsync(this);

        mSearchViewModel.getClearMapObservable().addObserver((o, arg) -> clearMap());

        mSearchViewModel.getZoomMapObservable().addObserver((o, arg) -> {
            if (arg instanceof Location)
                zoomOnLocation((Location) arg);
        });

        mSearchViewModel.getAddMapMarker().addObserver((o, arg) -> {
            MarkerAddMessage state = (arg instanceof MarkerAddMessage ? (MarkerAddMessage) arg : null);
            if (mGoogleMap != null) {
                mMarkerStringHashMap.put(mGoogleMap.addMarker(Objects.requireNonNull(state).getMarkerOptions()), state.getPlaceID());
            }
        });

        return mBinding.getRoot();
    }

    // ------------------------
    // Override
    // ------------------------
    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        // Save Google Map
        mGoogleMap = googleMap;
        // Set info
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));

        // Fix Warning
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        zoomOnLocation(getMyLocation());

        // Clear in case
        clearMap();

        // Perform an Automated empty search
        SearchValidateMessage svd = new SearchValidateMessage()
                .setSearchMethod(SearchType.CLOSER);
        mSearchViewModel.setSearchValidationDataViewMutable(svd);

        // Open Restaurant detail when click on info in marker
        googleMap.setOnInfoWindowClickListener(marker ->
                openDetailRestaurant(requireActivity(), mMarkerStringHashMap.get(marker)));
    }

    @Override
    public void onResume() {
        super.onResume();

        clearMap();

        SearchValidateMessage svd = new SearchValidateMessage()
                .setSearchMethod(SearchType.CLOSER);
        mSearchViewModel.setSearchValidationDataViewMutable(svd);
        mBinding.mapView.onResume();
    }

    private void clearMap() {
        if (mGoogleMap != null)
            mGoogleMap.clear();
        mMarkerStringHashMap.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.mapView.onLowMemory();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBinding.mapView.onStop();
    }

    // ------------------------
    // Methods
    // ------------------------
    public void zoomOnLocation(Location location) {
        if (location != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}