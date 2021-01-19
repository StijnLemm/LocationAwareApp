package com.study.locationawareapp.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class Subject {
    private final int arg;
    List<Observer> observers;

    public Subject(int arg) {
        this.observers = new ArrayList<>();
        this.arg = arg;
    }

    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer :
                observers) {
            observer.update(null, arg);
        }
    }
}
