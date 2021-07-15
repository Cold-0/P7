package com.openclassroom.go4lunch.utils.ex;

import androidx.fragment.app.Fragment;

import com.openclassroom.go4lunch.repository.Repository;

public abstract class FragmentEX extends Fragment {

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public FragmentEX() {
        mRepository = Repository.getRepository();
    }
}
