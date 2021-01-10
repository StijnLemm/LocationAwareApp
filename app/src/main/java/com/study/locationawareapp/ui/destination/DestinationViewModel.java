package com.study.locationawareapp.ui.destination;


import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DestinationViewModel extends ViewModel implements ListProvider{

    private final DestinationModel destinationModel;

    public DestinationViewModel() {
        this.destinationModel = new DestinationModel();

        // Create the binding when something in the data is updated we get notified
//        this.destinationModel.getCurrentDestination().observe(this, (Observer<Destination>) destination -> {
//            //todo do something with the destination
//        });
    }

    public int size() {
        return destinationModel.getPastDestinations().getValue().size();
    }

    @Override
    public ArrayList<Destination> getList() {
        return destinationModel.getPastDestinations().getValue();
    }
}