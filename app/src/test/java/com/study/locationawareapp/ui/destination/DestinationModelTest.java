package com.study.locationawareapp.ui.destination;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertNotNull;

public class DestinationModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private DestinationModel destinationModel;

    @Before
    public void setUp() throws Exception {
        this.destinationModel = new DestinationModel();
    }

    @Test
    public void setCurrentDestination() {
        this.destinationModel.setCurrentDestination(new Destination("test", 0, 0, 0));
        assertNotNull(this.destinationModel.getCurrentDestination());
    }

    @Test
    public void getCurrentDestination() {
        this.destinationModel.setCurrentDestination(new Destination("test", 0, 0, 0));
        assertNotNull(this.destinationModel.getCurrentDestination());
    }
}