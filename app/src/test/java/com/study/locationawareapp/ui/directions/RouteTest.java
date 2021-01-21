package com.study.locationawareapp.ui.directions;

import com.study.locationawareapp.R;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class RouteTest {

    private Route route;
    private HashMap<Integer, GeoPoint>  coordinates;
    private ArrayList<Step> steps;
    private int distance;
    private int duration;
    private GeoPoint closestCoordinate;
    private GeoPoint farthestCoordinate;


    @Before
    public void setUp() {
        this.closestCoordinate = new GeoPoint(0.0,0.0);
        this.farthestCoordinate = new GeoPoint(0.3,0.0);

        coordinates = new HashMap<>();

        coordinates.put(0,closestCoordinate);
        coordinates.put(1,new GeoPoint(0.1,0.0));
        coordinates.put(2,new GeoPoint(0.2,0.0));
        coordinates.put(3,farthestCoordinate);


        steps = new ArrayList<>();

        steps.add(new Step(0,0,0,"test","test",0,1));
        steps.add(new Step(0,0,0,"test","test",1,2));
        steps.add(new Step(0,0,0,"test","test",2,3));

        distance = 1000;
        duration = 60;

        this.route = new Route(coordinates,steps,distance,duration);
    }

    @Test
    public void getCoordinates(){
        HashMap<Integer, GeoPoint> coordinates = route.getCoordinates();

        assertEquals(coordinates,this.coordinates);
    }

    @Test
    public void getSteps(){
        ArrayList<Step> steps = route.getSteps();

        assertEquals(steps,this.steps);
    }

    @Test
    public void getClosestCoordinate(){
        GeoPoint currentLocation = new GeoPoint(0.001,0.0);
        double smallestDistance= currentLocation.distanceToAsDouble(closestCoordinate);
        double distance = route.distanceToClosestCoordinte(currentLocation);

        assertEquals(smallestDistance,distance,0.0001);
    }

    @Test
    public void hasVisitedGeoPoint(){
        GeoPoint currentLocation = new GeoPoint(0.0,0.0);

        boolean notVisited = route.hasVisitedGeoPoint(currentLocation);

        assertFalse("Closest point is first point so should not change",notVisited);

        currentLocation.setCoords(0.1,0.0);
        boolean visited = route.hasVisitedGeoPoint(currentLocation);

        assertTrue("Closest point is second point",visited);
    }

    @Test
    public void hasVisitedwierdCases(){
        GeoPoint currentLocation = new GeoPoint(0.1,0.0);

        Route route = new Route(new HashMap<>(),steps,distance,duration);
        route.hasVisitedGeoPoint(currentLocation);
    }

}