package com.study.locationawareapp.ui.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class TravelProfileTest {

    @Test
    public void values() {
        TravelProfile[] values = TravelProfile.values();
        assertTrue(values.length>0);
    }

    @Test
    public void valueOf() {
        TravelProfile car = TravelProfile.valueOf("car");
        assertNotNull(car);
    }
}