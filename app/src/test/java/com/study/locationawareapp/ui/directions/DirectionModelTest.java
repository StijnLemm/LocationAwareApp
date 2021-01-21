package com.study.locationawareapp.ui.directions;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class DirectionModelTest {

    private DirectionModel directionModel;
    private ArrayList<Step> steps;
    private HashMap<Integer, GeoPoint> coordinates;
    private Route route;

    @Before
    public void setUp() {
        this.directionModel = new DirectionModel();

        GeoPoint closestCoordinate = new GeoPoint(0.0,0.0);
        GeoPoint farthestCoordinate = new GeoPoint(0.1,0.0);

        this.coordinates = new HashMap<>();

        coordinates.put(0,closestCoordinate);
        coordinates.put(1,farthestCoordinate);

        this.steps = new ArrayList<>();
        this.steps.add(new Step(0,0,0,"test","test",0,1));

        double distance = 1000;
        int duration = 60;

        this.route = new Route(coordinates,steps,distance,duration);

        directionModel.setRoute(route);
    }

    @Test
    public void getSetRoute(){
        assertEquals(route,directionModel.getRoute());

        directionModel = new DirectionModel();
        assertNull(directionModel.getRoute());

    }

    @Test
    public void getSteps(){
        assertEquals(steps,directionModel.getSteps());

        DirectionModel nullTest = new DirectionModel();
        assertNotEquals(steps,nullTest.getSteps());
    }

    @Test
    public void getCoordinates(){
        assertEquals(coordinates,directionModel.getCoordinates());

        DirectionModel nullTest = new DirectionModel();
        assertNotEquals(coordinates,nullTest.getCoordinates());
    }

}