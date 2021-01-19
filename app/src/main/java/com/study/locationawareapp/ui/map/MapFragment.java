package com.study.locationawareapp.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.study.locationawareapp.App;
import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.POIsHolder;
import com.study.locationawareapp.ui.destination.Destination;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements View.OnClickListener, MapController, Observer {

    private MapView mapView;
    private MapViewModel mapViewModel;
    private AppViewModel appViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        this.appViewModel =
                new ViewModelProvider(this.getActivity()).get(AppViewModel.class);

        appViewModel.subject.attachObserver(this);

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mapView = view.findViewById(R.id.MapView_Map);
        this.mapViewModel.setController(this);
        this.mapViewModel.initMapView(this.mapView);

        FloatingActionButton fab = view.findViewById(R.id.Button_CenterMapButton);
        fab.setOnClickListener(this);
    }

    @Override
    public void setCenter(GeoPoint location) {
        this.appViewModel.getPOIs(location);
        this.getActivity().runOnUiThread(() -> {
            this.mapView.getController().setCenter(location);
        });
    }

    @Override
    public void setCenterAnimated(GeoPoint location) {
        this.getActivity().runOnUiThread(() -> {
            this.mapView.getController().zoomTo(18d);
            this.mapView.getController().animateTo(location);
        });
    }

    public void drawPOIs(List<Destination> pois) {
        if (this.getActivity() != null) {
            this.getActivity().runOnUiThread(() -> {
                for (Destination destination : pois) {
                    GeoPoint point = new GeoPoint(destination.getLatitude(), destination.getLongitude());

                    Marker marker = new Marker(mapView);
                    marker.setTitle(destination.getName());
                    marker.setPosition(point);
                    mapView.getOverlays().add(marker);
                }
                mapView.invalidate();
            });
        }
    }

    public void drawRoute() {

        Polyline polyline = new Polyline();
        ArrayList<GeoPoint> coordinates = appViewModel.getRouteCoordinates();

        polyline.setPoints(coordinates);

        mapView.getOverlayManager().add(polyline);

        mapView.invalidate();

        Log.d(TAG, "drawRoute: done drawing line");
    }

    //fab button click
    @Override
    public void onClick(View view) {
        this.mapViewModel.centerMapAnimated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void update(Observable o, Object arg) {
        //todo get the pois and use them
        List<Destination> destinations = this.appViewModel.getLoadedPOIs();
        drawPOIs(destinations);
        drawRoute();
    }
}