package com.study.locationawareapp.ui.map;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureDetector;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

public class MapViewModel extends ViewModel {

    private MapView mapView;
    private IMapController controller;
    private RotationGestureOverlay rotationGestureOverlay;

    public MapViewModel() {
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        this.initMapView();
    }

    private void initMapView(){
        this.mapView.setTileSource(TileSourceFactory.MAPNIK);
        this.mapView.setUseDataConnection(true);

        this.controller = this.mapView.getController();
        this.controller.setZoom(15d);

        this.rotationGestureOverlay = new RotationGestureOverlay(this.mapView);
        this.rotationGestureOverlay.setEnabled(true);
        this.mapView.setMultiTouchControls(true);
        this.mapView.getOverlays().add(this.rotationGestureOverlay);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
    }

    private GeoPoint lastLocation;

    public void setLastLocation(GeoPoint location) {

        if(this.lastLocation == null){
            this.lastLocation = location;
            return;
        }

        this.rotateMap(location);
        this.controller.animateTo(location);
        this.lastLocation = location;
    }

    private void rotateMap(GeoPoint nextPoint) {
        
    }
}