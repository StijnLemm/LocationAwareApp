package com.study.locationawareapp.ui.directions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StepTest {

    private Step step;
    private String instruction;
    private String streetName;
    private double distance;
    private int endWayPoint;
    private String time;

    @Before
    public void setUp() {
        this.instruction = "Go left";
        this.streetName = "Lovendijkstraat";
        this.distance = 1600;
        this.endWayPoint = 8;
        this.time = "1:15";
        this.step = new Step(distance,75,0,instruction,streetName,0, endWayPoint);
    }

    @Test
    public void InstructionGetter(){
        assertEquals("The instruction is not saved or retrieved properly",instruction,step.getInstruction());
    }

    @Test
    public void distanceGetter(){
        assertEquals("The distance in km is not saved or retrieved properly",String.format("%.2f", this.distance / 1000.0),step.getDistanceInKM());
    }

    @Test
    public void streetNameGetter(){
        assertEquals("The street name is not saved or retrieved properly",streetName,step.getStreetName());
    }

    @Test
    public void Getter(){
        assertEquals("The way point is not saved properly", endWayPoint,step.getEndWayPoint());
    }

    @Test
    public void timeFormatGetter(){
        assertEquals("The time is not formatted correctly",time,step.getDurationInFormat());
    }
}