package com.study.locationawareapp.ui.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.study.locationawareapp.R;
import com.study.locationawareapp.services.LocationForegroundService;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

public class MapFragment extends Fragment {

    private MapViewModel dashboardViewModel;
    private BroadcastReceiver broadcastReceiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mapView = view.findViewById(R.id.MapView_Map);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        IMapController controller = mapView.getController();
        controller.setZoom(14d);

        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(rotationGestureOverlay);

        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(LocationForegroundService.GPS_SERVICE_INTENT_ACTION);
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double lon = intent.getDoubleExtra(LocationForegroundService.GPS_LONGITUDE_INTENT_KEY, Double.MAX_VALUE);
                double lat = intent.getDoubleExtra(LocationForegroundService.GPS_LATITUDE_INTENT_KEY, Double.MAX_VALUE);

                Log.v("BROADCAST", "lon: " + lon);
                Log.v("BROADCAST", "lat: " + lat);

                GeoPoint location = new GeoPoint(lon, lat);
                controller.setCenter(location);
            }
        };
        getContext().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }
}