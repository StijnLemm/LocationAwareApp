package com.study.locationawareapp.ui.destination;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DestinationModel {
    private MutableLiveData<Destination> currentDestination;

    public DestinationModel() {
                this.currentDestination = new MutableLiveData<>();
    }


    public void setCurrentDestination(Destination currentDestination) {
        this.currentDestination.postValue(currentDestination);
    }

    public MutableLiveData<Destination> getCurrentDestination() {
        return currentDestination;
    }

}
