package com.study.locationawareapp.ui.destination;

import org.osmdroid.util.GeoPoint;

public class Destination {
    private final int UICCode;
    private String name;
    private final double latitude;
    private final double longitude;
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

    public double getLongitude() {
        return longitude;
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude,longitude);
    }
}
