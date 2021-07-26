package com.openclassroom.go4lunch.viewmodels;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassroom.go4lunch.listeners.OnResponseListener;
import com.openclassroom.go4lunch.messages.MarkerAddMessage;
import com.openclassroom.go4lunch.messages.RestaurantAddMessage;
import com.openclassroom.go4lunch.messages.SearchValidateMessage;
import com.openclassroom.go4lunch.models.User;
import com.openclassroom.go4lunch.models.api.nearbysearch.NearbySearchResponse;
import com.openclassroom.go4lunch.models.api.nearbysearch.NearbySearchResult;
import com.openclassroom.go4lunch.models.api.placedetails.PlaceDetailsResult;
import com.openclassroom.go4lunch.R;
import com.openclassroom.go4lunch.types.SearchType;
import com.openclassroom.go4lunch.utils.ex.ObservableEX;
import com.openclassroom.go4lunch.utils.ex.ViewModelEX;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

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

    // ------------------------
    // Constructor
    // ------------------------
    public SearchViewModel(@NonNull @NotNull Application application) {
        super(application);

        mSearchValidationDataViewMutableLiveData.observeForever(
                searchValidationDataView ->
                        callUserList((currentUser, userList) ->
                                onSearchValidate(currentUser, userList, searchValidationDataView)));
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

    public MutableLiveData<SearchValidateMessage> getSearchValidationDataViewMutableLiveData() {
        return mSearchValidationDataViewMutableLiveData;
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
        if (searchValidationDataView.getSearchType() == SearchType.PREDICTION) {
            callPlaceDetails(searchValidationDataView.getPrediction().getPlaceId(), response ->
                    doNearbySearch(currentUser, userList, searchValidationDataView, response.getResult()));
        } else {
            doNearbySearch(currentUser, userList, searchValidationDataView, null);
        }
    }

    private void doNearbySearch(User currentUser, List<User> userList, SearchValidateMessage data, PlaceDetailsResult response) {
        mClearMapObservable.notifyObservers();
        Location loc = new Location("");
        loc.setLongitude(0.0);
        loc.setLatitude(0.0);
        if (response != null && data.getSearchType() == SearchType.PREDICTION) {
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
            state.setMarkerOptions(userIndicator);
            state.setPlaceID(response.getPlaceId());
            mAddMapMarker.notifyObservers(state);

        } else {
            loc = getMyLocation();
        }

        mZoomMapObservable.notifyObservers(loc);

        OnResponseListener<NearbySearchResponse> listener = listenerResponse -> doRestaurantAdd(currentUser, userList, listenerResponse);

        if (data.getSearchType() == SearchType.PREDICTION || data.getSearchType() == SearchType.CLOSER) {
            callNearbySearchByType(String.format(Locale.CANADA, getApplication().getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude()), "restaurant", listener);
        } else if (data.getSearchType() == SearchType.SEARCH_STRING) {
            callNearbySearchByKeyword(String.format(Locale.CANADA, getApplication().getString(R.string.location_formating), loc.getLatitude(), loc.getLongitude()), data.getSearchString(), listener);
        }
    }

    private void doRestaurantAdd(User currentUser, List<User> userList, @NotNull NearbySearchResponse response) {
        mClearRestaurantList.notifyObservers();
        for (NearbySearchResult nearbysearchresult : response.getResults()) {
            // Build LatLng instance
            Double lat = nearbysearchresult.getGeometry().getLocation().getLat();
            Double lng = nearbysearchresult.getGeometry().getLocation().getLng();
            LatLng loc = new LatLng(lat, lng);

            // Default Red
            float hue = BitmapDescriptorFactory.HUE_RED;

            // If anyone EatAt then make it Green
            for (User user : userList) {
                if (nearbysearchresult.getPlaceId().equals(user.getEatingAt())) {
                    hue = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                }
            }

            // If you EatAt then make it Blue
            if (nearbysearchresult.getPlaceId().equals(currentUser.getEatingAt()))
                hue = BitmapDescriptorFactory.HUE_AZURE;

            // Build MarkerOptions
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(loc)
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))
                    .title(nearbysearchresult.getName())
                    .snippet(nearbysearchresult.getVicinity());

            // Build MarkerAddMessage
            MarkerAddMessage state = new MarkerAddMessage()
                    .setMarkerOptions(userIndicator)
                    .setPlaceID(nearbysearchresult.getPlaceId());

            // Build RestaurantAddMessage
            RestaurantAddMessage restaurantInfoState = new RestaurantAddMessage()
                    .setNearbySearchResult(nearbysearchresult)
                    .setUserList(userList);

            // Notify observer and send Messages
            mAddMapMarker.notifyObservers(state);
            mAddRestaurantToList.notifyObservers(restaurantInfoState);
        }
    }
}