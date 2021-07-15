package com.openclassroom.go4lunch.ViewModel.Utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.openclassroom.go4lunch.Repository.Repository;

import org.jetbrains.annotations.NotNull;

public class ViewModelX extends AndroidViewModel {

    protected final Repository mRepository;

    public ViewModelX(@NonNull @NotNull Application application) {
        super(application);
        mRepository = Repository.getRepository();
    }

    @NonNull
    public Application requireApplication() {
        return getApplication();
    }
}
