package com.study.locationawareapp.ui.map;

import android.content.Context;

import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapViewModelTest extends TestCase implements MapController {

    private Context context;
    private MapViewModel mapViewModel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mapViewModel = new MapViewModel();
        this.context = mock(Context.class);
        MyLocationNewOverlay myLocationNewOverlay = mock(MyLocationNewOverlay.class);
        when(myLocationNewOverlay.getMyLocation()).thenReturn(new GeoPoint(0d,0d));
        this.mapViewModel.setMyLocationNewOverlay(myLocationNewOverlay);
    }

    public void testGetLastLocation() {
        assertNotNull(this.mapViewModel.getLastLocation());
    }

    public void testSetController() {
        this.mapViewModel.setController(this);
    }

    public void testCenterMapAnimated() {
        this.mapViewModel.setController(this);
        this.mapViewModel.centerMapAnimated();
    }

    @Override
    public void setCenter(GeoPoint location) {
        //nothing, just override.
    }

    @Override
    public void setCenterAnimated(GeoPoint location) {
        assertNotNull(location);
    }
}