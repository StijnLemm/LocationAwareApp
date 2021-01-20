package com.study.locationawareapp.ui.destination;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DestinationModel {
    private MutableLiveData<ArrayList<Destination>> pastDestinations;
    private MutableLiveData<Destination> currentDestination;

    public DestinationModel(){
        // We made it mutable live data because the duration and switches are recieved at a later date
        this.pastDestinations = new MutableLiveData<>();
        this.pastDestinations.setValue(new ArrayList<>());

        this.currentDestination = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Destination>> getPastDestinations() {
        return pastDestinations;
    }

    public void setCurrentDestination(Destination currentDestination) {
        this.currentDestination.postValue(currentDestination);
    }

    public void addPastDestination(Destination destinations) {
        ArrayList<Destination> list = this.pastDestinations.getValue();
        if (list.size() > 10)
            list.remove(0);

        list.add(destinations);

        pastDestinations.postValue(list);
    }

    public MutableLiveData<Destination> getCurrentDestination() {
        return currentDestination;
    }

}
