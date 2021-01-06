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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.study.locationawareapp.R;
import com.study.locationawareapp.services.LocationForegroundService;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

public class MapFragment extends Fragment {

    private MapView mapView;
    private MapViewModel mapViewModel;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mapView = view.findViewById(R.id.MapView_Map);
        this.mapViewModel.setMapView(this.mapView);
        this.registerGPSReceiver();
    }

    private void registerGPSReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationForegroundService.GPS_SERVICE_INTENT_ACTION);
        this.broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double lon = intent.getDoubleExtra(LocationForegroundService.GPS_LONGITUDE_INTENT_KEY, Double.MAX_VALUE);
                double lat = intent.getDoubleExtra(LocationForegroundService.GPS_LATITUDE_INTENT_KEY, Double.MAX_VALUE);

                GeoPoint location = new GeoPoint(lat, lon);
                mapViewModel.setLastLocation(location);
            }
        };
        getContext().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().getApplicationContext().unregisterReceiver(broadcastReceiver);
    }
}