package com.study.locationawareapp.ui.directions;

import com.study.locationawareapp.ui.destination.Destination;

import java.util.ArrayList;

public interface DirectionsListProvider {
    ArrayList<Step> getRouteSteps();
}
