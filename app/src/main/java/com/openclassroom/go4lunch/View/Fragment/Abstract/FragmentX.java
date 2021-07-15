package com.openclassroom.go4lunch.View.Fragment.Abstract;

import androidx.fragment.app.Fragment;

import com.openclassroom.go4lunch.Repository.Repository;

public class FragmentX extends Fragment {

    public Repository getRepository() {
        return mRepository;
    }

    private final Repository mRepository;

    public FragmentX() {
        mRepository = Repository.getRepository();
    }
}
