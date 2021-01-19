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

    public void initMapView(MapView mapView){
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);

        IMapController controller = mapView.getController();
        controller.setZoom(18d);

        this.myLocationNewOverlay = new MyLocationNewOverlay(mapView);
        this.myLocationNewOverlay.enableMyLocation();
        this.myLocationNewOverlay.setDrawAccuracyEnabled(true);
        this.myLocationNewOverlay.runOnFirstFix(this::centerMap);

        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);

        mapView.getOverlays().add(rotationGestureOverlay);
        mapView.getOverlays().add(this.myLocationNewOverlay);

        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
    }

    public GeoPoint getLastLocation(){
        return this.myLocationNewOverlay.getMyLocation();
    }

    private void centerMap(){
        this.mapController.setCenter(this.getLastLocation());
    }

    public void centerMapAnimated() {
        this.mapController.setCenterAnimated(this.getLastLocation());
    }

    public void setController(MapController mapController) {
        this.mapController = mapController;
    }
}