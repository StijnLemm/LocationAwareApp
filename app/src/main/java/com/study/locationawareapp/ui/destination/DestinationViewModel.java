package com.study.locationawareapp.ui.destination;


import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DestinationViewModel extends ViewModel implements ListProvider{

    private final DestinationModel destinationModel;

    public DestinationViewModel() {
        this.destinationModel = new DestinationModel();
    }


    public int size() {
        return destinationModel.getPastDestinations().size();
    }

    @Override
    public ArrayList<String> getList() {
        return destinationModel.getPastDestinations();
    }
}