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
        // Boolean to know if the list has been altered
        boolean listWasChanged = false;

        // All the keys available
        Set<Integer> keys = coordinates.keySet();
        // List of all the keys that will be removed
        ArrayList<Integer> removeables = new ArrayList<>();

        // Check if the size is bigger then 0 else we don't do anything
        if (keys.size() < 1)
            return false;

        // Keep the first digit so we know if we skipped a part of the route we can delete that part
        int firstPoint = (Integer) keys.toArray()[0];

        // Loop through all the keys
        for (Integer i : keys) {
            // If the distance is smaller then 10 meter between the current location and the geolocation
            if (coordinates.get(i).distanceToAsDouble(currentLocation) < 10) {
                // We set the boolean that something has changed
                listWasChanged = true;

                // Add the key to the removables
                removeables.add(i);
                // Check if the key is later then the start point of the route
                if (i > firstPoint)
                    // Remove all the point before the point we have visited
                    while (firstPoint < i) {
                        removeables.add(firstPoint);
                        firstPoint++;
                    }
            }
        }

        if (listWasChanged) {
            // Remove all the points that were needed to be removed
            coordinates.remove(removeables);

            // Loop through the steps and if we have visited all the point of that step we delete the step
            Iterator iterator = steps.iterator();
            while (iterator.hasNext()) {

                Step step = (Step) iterator;
                
                if (step.getEndWayPoint() < firstPoint)
                    iterator.remove();

                iterator.next();
            }
        }

        // Return if there has changed anything in the list
        return listWasChanged;
    }
}
