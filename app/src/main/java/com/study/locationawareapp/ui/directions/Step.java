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

    public String getInstruction() {
        return instruction;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getEndWayPoint() {
        return endWayPoint;
    }

    public String getDurationInFormat() {
        int minutes = duration % 60;
        int hours = (duration - minutes) / 60;

        return hours + ":" + minutes;
    }

    public String getDistanceInKM() {
        String distance = String.format("%.2f", this.distance / 1000.0);
        return distance;
    }
}
