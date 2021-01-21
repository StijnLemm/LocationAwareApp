package com.study.locationawareapp.ui.destination;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import static org.junit.Assert.*;

public class DestinationTest {

    private Destination destination;

    @Before
    public void setUp() throws Exception {
        this.destination = new Destination(null, 0, 1234, 1234);
    }

    @Test
    public void setName() {
        this.destination.setName("test");
        assertEquals("test", this.destination.getName());
    }

    @Test
    public void getLatitude() {
        assertEquals(1234, this.destination.getLatitude(), 0);
    }

    @Test
    public void getLongitude() {
        assertEquals(1234, this.destination.getLongitude(), 0);
    }

    @Test
    public void getGeoPoint() {
        GeoPoint geoPoint = this.destination.getGeoPoint();
        assertEquals(1234, geoPoint.getLatitude(), 0);
        assertEquals(1234, geoPoint.getLongitude(), 0);
    }

    @Test
    public void getUICCode() {
        assertEquals(0, this.destination.getUICCode());
    }

    @Test
    public void getName() {
        this.destination.setName("test");
        assertEquals("test", this.destination.getName());
    }
}