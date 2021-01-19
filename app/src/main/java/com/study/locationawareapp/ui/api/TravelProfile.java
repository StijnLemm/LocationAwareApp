package com.study.locationawareapp.ui.api;

public enum TravelProfile {
    car("driving-car"),
    hgv("driving-hgv"),
    walking("foot-walking"),
    wheelchair("foot-wheelchair"),
    hiking("foot-hiking"),
    cycling("cycling-regular"),
    cycling_mountain("cycling-mountain"),
    cycling_road("cycling-road"),
    cycling_electric("cycling-electric");

    public final String label;

    private TravelProfile(String label) {
        this.label = label;
    }
}
