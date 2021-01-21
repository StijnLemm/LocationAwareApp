package com.study.locationawareapp.ui;

import com.study.locationawareapp.ui.directions.Route;
import com.study.locationawareapp.ui.directions.Step;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public interface RouteHolder {
    void setRoute(Route route);
    void setTrainRoute(Polyline route);
    ArrayList<GeoPoint> getRouteCoordinates();
    ArrayList<Step> getRouteSteps();
}
