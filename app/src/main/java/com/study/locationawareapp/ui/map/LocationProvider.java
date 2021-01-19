package com.study.locationawareapp.ui.map;

import org.osmdroid.util.GeoPoint;

public interface LocationProvider {
    GeoPoint getLastLocation();
}
