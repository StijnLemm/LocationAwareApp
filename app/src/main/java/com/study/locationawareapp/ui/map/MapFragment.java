package com.study.locationawareapp.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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

        this.appViewModel.getPOIs(mapViewModel.getCurrentLocation());

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
        this.getActivity().runOnUiThread(() -> {
            this.mapView.getController().setCenter(location);
        });
    }

    @Override
    public void setCenterAnimated(GeoPoint location) {
        this.getActivity().runOnUiThread(() -> {
            this.mapView.getController().animateTo(location);
        });
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
    }
}