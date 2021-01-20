package com.study.locationawareapp.ui.destination;

import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

public class Destination {
    private final int UICCode;
    private String name;
    private double latitude;
    private double longitude;
    private double distance;

    public Destination(String name, int UICCode, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.UICCode = UICCode;
    }

    public int getUICCode() {
        return UICCode;
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

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude,longitude);
    }
}
