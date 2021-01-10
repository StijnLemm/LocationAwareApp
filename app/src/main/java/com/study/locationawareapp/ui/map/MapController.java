package com.study.locationawareapp.ui.map;

import org.osmdroid.util.GeoPoint;

public interface MapController {
    void setCenter(GeoPoint location);
    void setCenterAnimated(GeoPoint location);
}
