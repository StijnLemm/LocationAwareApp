package com.study.locationawareapp.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.destination.Destination;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class MapFragment extends Fragment implements View.OnClickListener, MapController, Observer, TrainRouteView {

    private static final String POI_KEY = "POI";
    private static final String SAVED_DOY_KEY = "DOY";
    private static final String PREVIOUS_POI_KEY = "PREVIOUS_POI";

    private MapView mapView;
    private MapViewModel mapViewModel;
    private AppViewModel appViewModel;
    private List<Destination> pois;
    private Polyline routeDrawing;
    private Polyline trainDrawing;
    private ArrayList<Destination> previousPois;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.mapViewModel =
                new ViewModelProvider(this.getActivity()).get(MapViewModel.class);
        this.appViewModel =
                new ViewModelProvider(this.getActivity()).get(AppViewModel.class);

        appViewModel.setLocationProvider(mapViewModel);
        appViewModel.setTrainRouteView(this);
        appViewModel.poiChangedSubject.attachObserver(this);

        this.pois = this.loadSavedPois();
        this.routeDrawing = new Polyline();
        this.trainDrawing = new Polyline();

        appViewModel.routeChangedSubject.attachObserver(this);

        appViewModel.setPreviousPOIs(loadPreviousPOIs());
        appViewModel.previousPOIsChangedSubject.attachObserver(this);

        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mapView = view.findViewById(R.id.MapView_Map);
        this.mapViewModel.setController(this);
        this.initMapView(this.mapView);

        appViewModel.setPOIs(pois);


        FloatingActionButton fab = view.findViewById(R.id.Button_CenterMapButton);
        fab.setOnClickListener(this);
    }

    public void initMapView(MapView mapView){
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);

        IMapController controller = mapView.getController();
        controller.setZoom(18d);


        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(mapView);
        myLocationNewOverlay.enableMyLocation();
        myLocationNewOverlay.setDrawAccuracyEnabled(true);
        myLocationNewOverlay.runOnFirstFix(() -> {
            this.setCenter(myLocationNewOverlay.getMyLocation());
        });

        this.mapViewModel.setMyLocationNewOverlay(myLocationNewOverlay);

        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);

        mapView.getOverlays().add(rotationGestureOverlay);
        mapView.getOverlays().add(myLocationNewOverlay);

        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
    }

    /*
    only called when loading fragment view
     */
    @Override
    public void setCenter(GeoPoint location) {
        this.loadPois(location);
        if (getActivity() != null)
            this.getActivity().runOnUiThread(() -> {
                this.mapView.getController().setCenter(location);
            });
    }

    @Override
    public void setCenterAnimated(GeoPoint location) {
        if (getActivity() != null)
            this.getActivity().runOnUiThread(() -> {
                this.mapView.getController().animateTo(location, 18d, 500L);
            });
    }

    public void drawPOIs(List<Destination> pois) {
        if (this.getActivity() != null)
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

    @Override
    public void drawTrainRoute(Polyline polyline) {
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> {
                //TODO stippeltjes/streepjes :)
                mapView.getOverlayManager().remove(this.trainDrawing);
                polyline.getOutlinePaint().setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
                polyline.getOutlinePaint().setColor(getResources().getColor(R.color.blue_2));

                mapView.getOverlayManager().add(polyline);
                mapView.invalidate();
                this.trainDrawing = polyline;
            });
    }

    public void drawRoute() {
        if (getActivity() != null)
            getActivity().runOnUiThread(() -> {

                mapView.getOverlayManager().remove(routeDrawing);

                this.routeDrawing = new Polyline();
                ArrayList<GeoPoint> coordinates = appViewModel.getRouteCoordinates();

                routeDrawing.setPoints(coordinates);

                mapView.getOverlayManager().add(routeDrawing);

                mapView.invalidate();
            });

        Log.d(TAG, "drawRoute: done drawing line");
    }


    private void loadPois(GeoPoint location) {
        if (this.pois.size() == 0) {
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
        if (getActivity() == null)
            return;

        //getting the current time in milliseconds, and creating a Date object from it:
        Calendar calendar = Calendar.getInstance(); //or simply new Date();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        if (Math.abs(dayOfYear - this.savedDate.get(Calendar.DAY_OF_YEAR)) > 0
                || (year - this.savedDate.get(Calendar.YEAR)) > 0) {

            SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();

            //converting it back to a milliseconds representation:
            long millis = calendar.getTimeInMillis();
            editor.putLong(SAVED_DOY_KEY, millis);

            Set<String> destinationStrings = new HashSet<>();

            for (Destination destination : destinations) {
                Gson gson = new Gson();
                String destinationString = gson.toJson(destination);
                destinationStrings.add(destinationString);
            }

            editor.putStringSet(POI_KEY, destinationStrings);
            editor.apply();

            this.savedDate = calendar;
        }
    }

    public List<Destination> loadSavedPois() {
        List<Destination> destinations = new ArrayList<>();

        SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mPrefs.getLong(SAVED_DOY_KEY, Long.MAX_VALUE));
        this.savedDate = calendar;

        Gson gson = new Gson();
        Set<String> json = mPrefs.getStringSet(POI_KEY, null);

        if (json != null) {
            for (String jsonDestination : json) {
                Destination destination = gson.fromJson(jsonDestination, Destination.class);
                destinations.add(destination);
            }
        }


        return destinations;
    }

    public ArrayList<Destination> loadPreviousPOIs() {
        ArrayList<Destination> previousPOIs = new ArrayList<>();
        if (getActivity() != null) {
            Gson gson = new Gson();

            SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);

            Set<String> previousPOIsJson = mPrefs.getStringSet(PREVIOUS_POI_KEY, null);

            if (previousPOIsJson != null) {
                for (String jsonDestination : previousPOIsJson) {
                    Destination destination = gson.fromJson(jsonDestination, Destination.class);
                    previousPOIs.add(destination);
                }
            }
        }
        this.previousPois = previousPOIs;
        return previousPOIs;
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals(0)) {
            List<Destination> destinations = this.appViewModel.getDestinationList();

            this.savePoisIfNeeded(destinations);

            drawPOIs(destinations);
        } else if (arg.equals(1)) {
            drawRoute();
        } else if (arg.equals(2)) {
            ArrayList<Destination> previousPOIs = appViewModel.getPreviousPOIsList();
            this.savePreviousPOIs(previousPOIs);
        }
    }

    private void savePreviousPOIs(ArrayList<Destination> previousPOIs) {
        if (getActivity() == null)
            return;

        Set<String> destinationStrings = new HashSet<>();
        SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();

        for (Destination prevPOI : previousPOIs) {
            Gson gson = new Gson();
            String destinationString = gson.toJson(prevPOI);
            destinationStrings.add(destinationString);
        }

        editor.putStringSet(PREVIOUS_POI_KEY, destinationStrings);
        editor.apply();
    }
}