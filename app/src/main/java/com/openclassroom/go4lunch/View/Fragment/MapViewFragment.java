package com.openclassroom.go4lunch.View.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.NearbySearchResponse;
import com.openclassroom.go4lunch.Model.NearbySearchAPI.Result;
import com.openclassroom.go4lunch.Model.PlaceDetailsAPI.PlaceDetailsResponse;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.Repository.Repository;
import com.openclassroom.go4lunch.ViewModel.MapViewModel;
import com.openclassroom.go4lunch.databinding.FragmentMapviewBinding;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapViewFragment.class.toString();

    private MapViewModel mMapViewModel;
    private FragmentMapviewBinding mBinding;
    private GoogleMap mGoogleMap;
    Repository mRepository;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentMapviewBinding.inflate(inflater, container, false);
        mBinding.mapView.onCreate(savedInstanceState);

        mMapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        mRepository = Repository.getRepository();

        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapViewModel.getSelectedPrediction().observe(requireActivity(), this::OnPredictionSelected);
        mBinding.mapView.getMapAsync(this);
        return mBinding.getRoot();
    }

    // Callback when prediction is selected
    private void OnPredictionSelected(@NotNull com.openclassroom.go4lunch.Model.AutocompleteAPI.Prediction prediction) {
        // Get Details of the selected AutoComplete place (Get Position)
        Call<PlaceDetailsResponse> callDetails = mRepository.getService().getDetails(prediction.getPlaceId());
        callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsResponse> call, @NonNull Response<PlaceDetailsResponse> response) {
                OnDetailResponseFromPredictionSelected(response);
            }

            @Override
            public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
            }

        });
    }

    // Callback when detail from selected prediction is received
    private void OnDetailResponseFromPredictionSelected(@NotNull Response<PlaceDetailsResponse> response) {
        Location loc = new Location("");
        mGoogleMap.clear();
        if (response.body() != null) {
            Double lat = response.body().getResult().getGeometry().getLocation().getLat();
            Double lng = response.body().getResult().getGeometry().getLocation().getLng();
            loc.setLatitude(lat);
            loc.setLongitude(lng);

            // Add marker of choice position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(response.body().getResult().getName())
                    .snippet(response.body().getResult().getVicinity());

            mGoogleMap.addMarker(userIndicator);

            zoomOnLocation(loc);

            // Get Nearby Search Respond using the Details as location
            @SuppressLint("DefaultLocale") Call<NearbySearchResponse> callNearbySearch = mRepository.getService().getNearbyByKeyword(10000, String.format("%f,%f", lat, lng), "food");
            callNearbySearch.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    OnNearbySearchResponseFromDetailResponse(response);
                }

                @Override
                public void onFailure(Call<NearbySearchResponse> call, Throwable t) {
                }
            });
        }
    }

    // Callback when nearby search from detail position received
    private void OnNearbySearchResponseFromDetailResponse(@NotNull Response<NearbySearchResponse> response) {
        assert response.body() != null;
        for (Result result : response.body().getResults()) {
            @SuppressLint("DefaultLocale") Call<PlaceDetailsResponse> callDetails = mRepository.getService().getDetails(result.getPlaceId());
            callDetails.enqueue(new Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                    OnDetailResponsePerEachNearbySearchResult(response);
                }

                @Override
                public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {

                }
            });
        }
    }

    // Callback for Details of each result of the nearby search
    private void OnDetailResponsePerEachNearbySearchResult(@NotNull Response<PlaceDetailsResponse> response) {
        if (response.body() != null) {
            Double lat = response.body().getResult().getGeometry().getLocation().getLat();
            Double lng = response.body().getResult().getGeometry().getLocation().getLng();
            LatLng loc = new LatLng(lat, lng);

            // Add marker of user's position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(loc)
                    .title(response.body().getResult().getName())
                    .snippet(response.body().getResult().getVicinity());
            mGoogleMap.addMarker(userIndicator);
        }
    }

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

    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location loc = new Location("");
            loc.setLatitude(0.0);
            loc.setLongitude(0.0);
            return loc;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        Toast.makeText(getContext(), "onMapReady", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
        zoomOnLocation(getMyLocation());
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
    public void onResume() {
        super.onResume();
        mBinding.mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBinding.mapView.onStop();
    }
}