package com.openclassroom.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.openclassroom.go4lunch.message.MarkerAddMessage;
import com.openclassroom.go4lunch.message.RestaurantAddMessage;
import com.openclassroom.go4lunch.message.SearchValidateMessage;
import com.openclassroom.go4lunch.model.User;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResponse;
import com.openclassroom.go4lunch.model.api.nearbysearch.NearbySearchResult;
import com.openclassroom.go4lunch.model.api.placedetails.DetailsResult;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.type.SearchType;
import com.openclassroom.go4lunch.utils.ex.ObservableEX;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;
import com.openclassroom.go4lunch.listener.OnUserListUpdateListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModelEX {

    // ------------------------
    // Constructor
    // ------------------------
    private final MutableLiveData<SearchValidateMessage> mSearchValidationDataViewMutableLiveData = new MutableLiveData<>();

    private final ObservableEX mClearMapObservable = new ObservableEX();
    private final ObservableEX mZoomMapObservable = new ObservableEX();
    private final ObservableEX mAddRestaurantToList = new ObservableEX();
    private final ObservableEX mClearRestaurantList = new ObservableEX();
    private final ObservableEX mAddMapMarker = new ObservableEX();

    MutableLiveData<User> mCurrentUser;
    MutableLiveData<List<User>> mUserList;
    Observer<List<User>> mUserListObserver;

    // ------------------------
    // Constructor
    // ------------------------
    public SearchViewModel(@NonNull @NotNull Application application) {
        super(application);

        mSearchValidationDataViewMutableLiveData.observeForever(searchValidationDataView -> {
            callUserList((currentUser, userList) -> onSearchValidate(currentUser, userList, searchValidationDataView));
        });

        getRepository().updateUserList();
        mUserList = getRepository().getUsersListLiveData();

        mUserListObserver = users -> {
            FirebaseUser firebaseUser = getRepository().getCurrentFirebaseUser();
            for (User user : Objects.requireNonNull(getRepository().getUsersListLiveData().getValue())) {
                if (user.getUid().equals(firebaseUser.getUid()))
                    mCurrentUser.setValue(user);
            }
        };
    }


    // ------------------------
    // Override
    // ------------------------
    @Override
    protected void onCleared() {
        super.onCleared();
        mClearMapObservable.deleteObservers();
        mAddRestaurantToList.deleteObservers();
        mClearRestaurantList.deleteObservers();
        mZoomMapObservable.deleteObservers();
    }

    // ------------------------
    // Getter
    // ------------------------
    public ObservableEX getClearMapObservable() {
        return mClearMapObservable;
    }

    public ObservableEX getZoomMapObservable() {
        return mZoomMapObservable;
    }

    public ObservableEX getAddRestaurantToList() {
        return mAddRestaurantToList;
    }

    public ObservableEX getClearRestaurantList() {
        return mClearRestaurantList;
    }

    public ObservableEX getAddMapMarker() {
        return mAddMapMarker;
    }

    // ------------------------
    // Setter
    // ------------------------
    public void setSearchValidationDataViewMutable(SearchValidateMessage searchValidationDataViewMutable) {
        this.mSearchValidationDataViewMutableLiveData.setValue(searchValidationDataViewMutable);
    }

    // ------------------------
    // SearchValidate
    // ------------------------
    private void onSearchValidate(User currentUser, List<User> userList, @NotNull SearchValidateMessage searchValidationDataView) {
        // Get Details of the selected AutoComplete place (Get Position)
        if (searchValidationDataView.searchMethod == SearchType.PREDICTION) {
            callDetail(searchValidationDataView.prediction.getPlaceId(), detailsResult -> {
                doNearbySearch(currentUser, userList, searchValidationDataView, detailsResult);
            });
        } else {
            doNearbySearch(currentUser, userList, searchValidationDataView, null);
        }
    }

    private void doNearbySearch(User currentUser, List<User> userList, SearchValidateMessage data, DetailsResult response) {
        mClearMapObservable.notifyObservers();
        Location loc = new Location("");
        if (response != null && data.searchMethod == SearchType.PREDICTION) {
            Double lat = response.getGeometry().getLocation().getLat();
            Double lng = response.getGeometry().getLocation().getLng();
            loc.setLatitude(lat);
            loc.setLongitude(lng);

            // Add marker of choice position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .title(response.getName())
                    .snippet(response.getVicinity());


            MarkerAddMessage state = new MarkerAddMessage();
            state.markerOptions = userIndicator;
            state.placeID = response.getPlaceId();
            mAddMapMarker.notifyObservers(state);

        } else {
            loc = getMyLocation();
        }

        mZoomMapObservable.notifyObservers(loc);

        // Get Nearby Search Respond using the Details as location
        Call<NearbySearchResponse> callNearbySearch = null;

        if (data.searchMethod == SearchType.PREDICTION || data.searchMethod == SearchType.CLOSER) {
            callNearbySearch = getRepository().getRetrofitService().getNearbyByType(5000, String.format(Locale.CANADA, getApplication().getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude()), "restaurant");
        } else if (data.searchMethod == SearchType.SEARCH_STRING) {
            callNearbySearch = getRepository().getRetrofitService().getNearbyByKeyword(5000, String.format(Locale.CANADA, getApplication().getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude()), data.searchString);
        }

        assert callNearbySearch != null;
        callNearbySearch.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                doDetailFill(currentUser, userList, response);
            }

            @Override
            public void onFailure(@NotNull Call<NearbySearchResponse> call, @NotNull Throwable t) {
            }
        });
    }

    private void doDetailFill(User currentUser, List<User> userList, @NotNull Response<NearbySearchResponse> response) {
        assert response.body() != null;
        mClearRestaurantList.notifyObservers();
        for (NearbySearchResult result : response.body().getResults()) {
            if (response.body() != null) {
                Double lat = result.getGeometry().getLocation().getLat();
                Double lng = result.getGeometry().getLocation().getLng();
                LatLng loc = new LatLng(lat, lng);

                float hue = BitmapDescriptorFactory.HUE_RED;

                for (User user : userList) {
                    if (result.getPlaceId().equals(user.getEatingAt())) {
                        hue = BitmapDescriptorFactory.HUE_GREEN;
                        break;
                    }
                }

                if (result.getPlaceId().equals(currentUser.getEatingAt()))
                    hue = BitmapDescriptorFactory.HUE_AZURE;

                // Add marker of user's position
                MarkerOptions userIndicator = new MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .title(result.getName())
                        .snippet(result.getVicinity());

                MarkerAddMessage state = new MarkerAddMessage();
                state.markerOptions = userIndicator;
                state.placeID = result.getPlaceId();
                mAddMapMarker.notifyObservers(state);

                RestaurantAddMessage restaurantInfoState = new RestaurantAddMessage();
                restaurantInfoState.result = result;
                restaurantInfoState.userList = userList;
                mAddRestaurantToList.notifyObservers(restaurantInfoState);
            }
        }
    }

    // ------------------------
    // Call
    // ------------------------
    public void callUserList(OnUserListUpdateListener listener) {
        getRepository().callUserList(listener);
    }
}