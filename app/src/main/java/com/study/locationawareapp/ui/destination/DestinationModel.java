package com.study.locationawareapp.ui.destination;

import java.util.ArrayList;

public class DestinationModel {
    private ArrayList<Destination> pastDestinations;
    private Destination currentDestination;

    public DestinationModel() {
        //todo get them from shared preferences
        this.pastDestinations = new ArrayList<>();

        this.pastDestinations.add(new Destination("Breda",51.5955543518066,4.78000020980835));
        this.pastDestinations.add(new Destination("Breda-Prinsenbeek",51.606388092041, 4.72083330154419));
    }

    public ArrayList<Destination> getPastDestinations() {
        return pastDestinations;
    }
}
