package com.openclassroom.go4lunch.utils.ex;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.openclassroom.go4lunch.model.placedetailsapi.PlaceDetailsResponse;
import com.openclassroom.go4lunch.repository.Repository;
import com.openclassroom.go4lunch.viewmodel.listener.OnDetailResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class ViewModelEX extends AndroidViewModel {

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public ViewModelEX(@NonNull @NotNull Application application) {
        super(application);
        mRepository = Repository.getRepository();
    }

    @NonNull
    public Application requireApplication() {
        return getApplication();
    }

    public void getDetails(String placeID, OnDetailResponse onDetailResponse) {
        getRepository().getDetails(placeID, onDetailResponse);
    }
}
