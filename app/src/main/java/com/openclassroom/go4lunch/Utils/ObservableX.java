package com.openclassroom.go4lunch.Utils;

import java.util.Observable;

public class ObservableX extends Observable {
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
