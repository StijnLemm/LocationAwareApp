package com.study.locationawareapp.ui;

import com.study.locationawareapp.ui.destination.Destination;

import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public interface POIsHolder {
    void fillPOIs(ArrayList<Destination> pois);
}
