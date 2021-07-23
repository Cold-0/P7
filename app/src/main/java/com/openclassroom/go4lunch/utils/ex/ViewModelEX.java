package com.openclassroom.go4lunch.utils.ex;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;

import com.openclassroom.go4lunch.listener.OnAutoCompleteResponse;
import com.openclassroom.go4lunch.listener.OnUserListUpdateListener;
import com.openclassroom.go4lunch.message.UserListUpdateMessage;
import com.openclassroom.go4lunch.repository.Repository;
import com.openclassroom.go4lunch.listener.OnDetailResponse;

import org.jetbrains.annotations.NotNull;

abstract public class ViewModelEX extends AndroidViewModel {

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public ViewModelEX(@NonNull @NotNull Application application) {
        super(application);
        mRepository = Repository.getRepository();
    }

    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location loc = new Location("");
            loc.setLatitude(0.0);
            loc.setLongitude(0.0);
            return loc;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    public void getDetailResponse(String placeID, OnDetailResponse onDetailResponse) {
        getRepository().getDetailResponse(placeID, onDetailResponse);
    }

    public void getAutocompleteResponse(String text, String localisation, String type, OnAutoCompleteResponse onAutoCompleteResponse) {
        getRepository().getAutocompleteResponse(text, localisation, type, onAutoCompleteResponse);
    }

    public void updateUserList(OnUserListUpdateListener listener) {
        getRepository().updateUserList();
        getRepository().getOnUpdateUsersList().addObserver((o, arg) -> {
            UserListUpdateMessage userListUpdateState = (UserListUpdateMessage) arg;
            listener.onUserListUpdated(userListUpdateState.currentUser, userListUpdateState.userList);
        });
    }


}
