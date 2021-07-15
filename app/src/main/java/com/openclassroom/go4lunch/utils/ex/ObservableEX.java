package com.openclassroom.go4lunch.utils.ex;

import java.util.Observable;

public class ObservableEX extends Observable {
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}