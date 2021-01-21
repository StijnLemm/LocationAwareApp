package com.study.locationawareapp.ui.directions;

import androidx.lifecycle.LiveData;

import com.study.locationawareapp.ui.api.CustomJSONParser;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class DirectionModel {
    private Route route;

    public void setRoute(Route route) {
        this.route = route;
    }

    public HashMap<Integer,GeoPoint> getCoordinates() {
        if (this.route == null)
            return new HashMap<>();

        return this.route.getCoordinates();
    }

    public ArrayList<Step> getSteps() {
        if (this.route == null)
            return new ArrayList<>();

        return this.route.getSteps();
    }

    public Route getRoute() {
        return route;
    }
}
