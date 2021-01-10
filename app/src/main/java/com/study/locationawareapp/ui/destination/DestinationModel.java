package com.study.locationawareapp.ui.destination;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DestinationModel {
    private MutableLiveData<ArrayList<Destination>> pastDestinations;
    private MutableLiveData<Destination> currentDestination;

    public DestinationModel() {
        // We made it mutable live data because the duration and switches are recieved at a later date
        this.pastDestinations = new MutableLiveData<>();
        this.pastDestinations.setValue(new ArrayList<>());

        this.pastDestinations.getValue().add(new Destination("Breda",51.5955543518066,4.78000020980835));
        this.pastDestinations.getValue().add(new Destination("Breda-Prinsenbeek",51.606388092041, 4.72083330154419));
    }

    public MutableLiveData<ArrayList<Destination>> getPastDestinations() {
        return pastDestinations;
    }

    public MutableLiveData<Destination> getCurrentDestination() {
        return currentDestination;
    }
}
