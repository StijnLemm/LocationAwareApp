package com.study.locationawareapp.ui.directions;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class DirectionModel {
        private Route route;

    public void setRoute(Route route) {
        this.route = route;
    }

    public ArrayList<GeoPoint> getCoordinates() {
        if (this.route.getCoordinates() == null)
            return new ArrayList<>();

        return this.route.getCoordinates();
    }

    public ArrayList<Step> getSteps() {
        if (this.route.getSteps() == null)
            return new ArrayList<>();

        return this.route.getSteps();
    }
}
