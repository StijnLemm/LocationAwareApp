package com.study.locationawareapp.ui;

import com.study.locationawareapp.ui.directions.Route;
import com.study.locationawareapp.ui.directions.Step;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public interface RouteHolder {
    void setRoute(Route route);
    ArrayList<GeoPoint> getRouteCoordinates();
    ArrayList<Step> getRouteSteps();
}
