package com.openclassroom.go4lunch.ViewModel.Abstract;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.openclassroom.go4lunch.Repository.Repository;

import org.jetbrains.annotations.NotNull;

abstract public class ViewModelX extends AndroidViewModel {

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public ViewModelX(@NonNull @NotNull Application application) {
        super(application);
        mRepository = Repository.getRepository();
    }

    @NonNull
    public Application requireApplication() {
        return getApplication();
    }
}
