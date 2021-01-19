package com.study.locationawareapp.ui.directions;

public class Step {
    private double distance;
    private int duration;
    private int type;
    private String instruction;
    private String streetName;
    private int startWayPoint;
    private int endWayPoint;

    public Step(double distance, int duration, int type, String instruction, String streetName, int startWayPoint, int endWayPoint) {
        this.distance = distance;
        this.duration = duration;
        this.type = type;
        this.instruction = instruction;
        this.streetName = streetName;
        this.startWayPoint = startWayPoint;
        this.endWayPoint = endWayPoint;
    }
}
