package com.project.agu.steamstalk.Services;

import java.util.Observable;

class ObservableObject extends Observable {
    private static final ObservableObject ourInstance = new ObservableObject();

    static ObservableObject getInstance() {
        return ourInstance;
    }

    private ObservableObject() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}
