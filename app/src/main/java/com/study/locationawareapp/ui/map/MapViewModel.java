package com.study.locationawareapp.ui.map;

import androidx.lifecycle.ViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapViewModel extends ViewModel implements LocationProvider{

    private MyLocationNewOverlay myLocationNewOverlay;
    private MapController mapController;

    public void setMyLocationNewOverlay(MyLocationNewOverlay myLocationNewOverlay) {
        this.myLocationNewOverlay = myLocationNewOverlay;
    }

    public GeoPoint getLastLocation(){
        return this.myLocationNewOverlay.getMyLocation();
    }

    public void centerMapAnimated() {
        this.mapController.setCenterAnimated(this.getLastLocation());
    }

    public void setController(MapController mapController) {
        this.mapController = mapController;
    }
}