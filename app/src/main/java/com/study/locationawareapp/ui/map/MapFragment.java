package com.study.locationawareapp.ui.map;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.destination.Destination;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements View.OnClickListener, MapController, Observer {

    private static final String POI_KEY = "POI";
    private static final String SAVED_DOY_KEY = "DOY";

    private MapView mapView;
    private MapViewModel mapViewModel;
    private AppViewModel appViewModel;
    private List<Destination> pois;

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

        this.pois = this.loadSavedPois();

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

    /*
    only called when loading fragment view
     */
    @Override
    public void setCenter(GeoPoint location) {

        this.loadPois(location);

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

        Log.d(TAG, "drawRoute: start drawing line");

        Polyline polyline = new Polyline();
        ArrayList<GeoPoint> coordinates = appViewModel.getRouteCoordinates();

        polyline.setPoints(coordinates);

        mapView.getOverlayManager().add(polyline);

        mapView.invalidate();

        Log.d(TAG, "drawRoute: done drawing line");
    }

    private void loadPois(GeoPoint location){
        if(this.pois.size() == 0){
            this.appViewModel.getPOIs(location);
        } else {
            drawPOIs(this.pois);
        }
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

    private Calendar savedDate;

    private void savePoisIfNeeded(List<Destination> destinations) {
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        if(Math.abs(dayOfYear - this.savedDate.get(Calendar.DAY_OF_YEAR)) > 0
                || (year - this.savedDate.get(Calendar.YEAR)) > 0){

            SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();

            //converting it back to a milliseconds representation:
            long millis = calendar.getTimeInMillis();
            editor.putLong(SAVED_DOY_KEY, millis);

            Set<String> destinationStrings = new HashSet<>();

            for(Destination destination : destinations){
                Gson gson = new Gson();
                String destinationString = gson.toJson(destination);
                destinationStrings.add(destinationString);
            }

            editor.putStringSet(POI_KEY, destinationStrings);
            editor.apply();

            this.savedDate = calendar;
        }
    }

    public List<Destination> loadSavedPois(){
        List<Destination> destinations = new ArrayList<>();

        SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mPrefs.getLong(SAVED_DOY_KEY, Long.MAX_VALUE));
        this.savedDate = calendar;

        Gson gson = new Gson();
        Set<String> json = mPrefs.getStringSet(POI_KEY, null);

        if(json != null){
            for(String jsonDestination : json){
                Destination destination = gson.fromJson(jsonDestination, Destination.class);
                destinations.add(destination);
            }
        }
        return destinations;
    }

    @Override
    public void update(Observable o, Object arg) {
        List<Destination> destinations = this.appViewModel.getLoadedPOIs();

        this.savePoisIfNeeded(destinations);

        drawPOIs(destinations);
        drawRoute();
    }
}