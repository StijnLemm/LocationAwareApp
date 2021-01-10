package com.study.locationawareapp.ui.destination;

import java.util.ArrayList;

public class DestinationModel {
    private ArrayList<String> pastDestinations;
    private Destination currentDestination;

    public DestinationModel() {
        //todo get them from shared preferences
        this.pastDestinations = new ArrayList<>();

        this.pastDestinations.add("Breda centraal station");
        this.pastDestinations.add("Breda princenbeek");
    }

    public ArrayList<String> getPastDestinations() {
        return pastDestinations;
    }
}
