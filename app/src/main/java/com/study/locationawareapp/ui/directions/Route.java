package com.study.locationawareapp.ui.directions;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Route {
    private HashMap<Integer, GeoPoint> coordinates;
    private ArrayList<Step> steps;
    private double distance;
    private int duration;

    public Route(HashMap<Integer, GeoPoint> coordinates, ArrayList<Step> steps, double distance, int duration) {
        this.coordinates = coordinates;
        this.steps = steps;
        this.distance = distance;
        this.duration = duration;
    }

    public HashMap<Integer, GeoPoint> getCoordinates() {
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

    /**
     * method that returns true if the list changed
     *
     * @param currentLocation GeoPoint of the user
     * @return true if list changed
     */
    public boolean hasVisitedGeoPoint(GeoPoint currentLocation) {
        boolean hasVisited = false;

        Set<Integer> keys = coordinates.keySet();
//        keys.removeIf(key -> {
//            if (coordinates.get(keys.).distanceToAsDouble(currentLocation) < 10) {
//                hasVisited = true;
//                return true;
//            }
//        });




        return hasVisited;
    }
}
