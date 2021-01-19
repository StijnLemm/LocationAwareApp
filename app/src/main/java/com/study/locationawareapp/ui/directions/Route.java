package com.study.locationawareapp.ui.directions;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Route {
    private ArrayList<GeoPoint> coordinates;
    private ArrayList<Step> steps;
    private double distance;
    private int duration;

    public Route(ArrayList<GeoPoint> coordinates, ArrayList<Step> steps, double distance, int duration) {
        this.coordinates = coordinates;
        this.steps = steps;
        this.distance = distance;
        this.duration = duration;
    }

    public ArrayList<GeoPoint> getCoordinates() {
        return coordinates;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public double getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
