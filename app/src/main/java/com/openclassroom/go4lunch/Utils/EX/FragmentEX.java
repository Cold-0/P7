package com.openclassroom.go4lunch.Utils.EX;

import androidx.fragment.app.Fragment;

import com.openclassroom.go4lunch.Repository.Repository;

public abstract class FragmentEX extends Fragment {

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public FragmentEX() {
        mRepository = Repository.getRepository();
    }
}
