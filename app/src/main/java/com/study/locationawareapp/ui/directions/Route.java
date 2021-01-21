package com.study.locationawareapp.ui.directions;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    /**
     * method that returns true if the list changed
     *
     * @param currentLocation GeoPoint of the user
     * @return true if list changed
     */
    public boolean hasVisitedGeoPoint(GeoPoint currentLocation) {
        // Boolean to know if the list has been altered
        boolean listWasChanged = false;

        // All the keys available
        Set<Integer> keys = coordinates.keySet();
        // List of all the keys that will be removed
        ArrayList<Integer> removables = new ArrayList<>();

        // Check if the size is bigger then 0 else we don't do anything
        if (keys.size() < 1)
            return false;

        // Keep the first digit so we know if we skipped a part of the route we can delete that part
        int firstPoint = (Integer) keys.toArray()[0];

        // Get closest coordinate
        GeoPoint closestCoordinate = getClosestCoordinate(currentLocation);

        // If the distance is smaller then 10 meter between the current location and the geolocation
        if (!closestCoordinate.equals(coordinates.get(firstPoint))) {
            // We set the boolean that something has changed
            listWasChanged = true;

            // Remove all the point before the point we have visited
            while (!closestCoordinate.equals(coordinates.get(firstPoint))) {
                removables.add(firstPoint);
                firstPoint++;
            }
        }


        if (listWasChanged) {
            // Remove all the points that were needed to be removed
            for (Integer i : removables) {
                coordinates.remove(i);
            }

            // Loop through the steps and if we have visited all the point of that step we delete the step
            for (int i = steps.size() - 1; i >= 0; i--) {
                Step step = steps.get(i);
                if (step.getEndWayPoint() <= firstPoint)
                    steps.remove(i);
            }


        }

        // Return if there has changed anything in the list
        return listWasChanged;
    }

    private GeoPoint getClosestCoordinate(GeoPoint currentLocation) {
        double minimalDistance = Double.MAX_VALUE;

        // Avoid null pointer if no coordinates are found
        GeoPoint closestCoordinate = new GeoPoint(currentLocation);

        Set<Integer> keys = coordinates.keySet();

        for (int key : keys) {
            double distance = coordinates.get(key).distanceToAsDouble(currentLocation);
            if (distance < minimalDistance) {
                minimalDistance = distance;
                closestCoordinate = coordinates.get(key);
            }
        }

        return closestCoordinate;
    }

    public double distanceToClosestCoordinte(GeoPoint currentLocation) {
        double minimalDistance = Double.MAX_VALUE;

        Set<Integer> keys = coordinates.keySet();

        for (int key : keys) {
            double distance = coordinates.get(key).distanceToAsDouble(currentLocation);
            if (distance < minimalDistance)
                minimalDistance = distance;
        }

        return minimalDistance;
    }
}
