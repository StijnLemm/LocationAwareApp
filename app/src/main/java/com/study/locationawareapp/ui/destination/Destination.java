package com.study.locationawareapp.ui.destination;

import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

public class Destination {
    private String name;
    private double latitude;
    private double longitude;
    private double distance;
    private int durationInMinutes;
    private int switches;

    public Destination(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.durationInMinutes = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDurationInMinutes() {
        int minutes = durationInMinutes % 60;
        int hours = (durationInMinutes - minutes) / 60;

        return hours + ":" + minutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public int getSwitches() {
        return switches;
    }

    public void setSwitches(int switches) {
        this.switches = switches;
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude,longitude);
    }
}
