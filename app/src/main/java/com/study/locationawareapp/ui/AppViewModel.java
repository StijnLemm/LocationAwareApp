package com.study.locationawareapp.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.study.locationawareapp.ui.api.APIModel;
import com.study.locationawareapp.ui.destination.Destination;
import com.study.locationawareapp.ui.destination.DestinationModel;
import com.study.locationawareapp.ui.destination.DestinationSetter;
import com.study.locationawareapp.ui.destination.DestinationListProvider;
import com.study.locationawareapp.ui.directions.DirectionModel;
import com.study.locationawareapp.ui.directions.DirectionsListProvider;
import com.study.locationawareapp.ui.directions.Route;
import com.study.locationawareapp.ui.directions.Step;
import com.study.locationawareapp.ui.map.LocationProvider;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class AppViewModel extends ViewModel implements DestinationSetter, DestinationListProvider, POIsHolder, RouteHolder, DirectionsListProvider {
    public Subject subject;
    private final DestinationModel destinationModel;
    private final DirectionModel directionModel;
    private final APIModel apiModel;
    private ArrayList<Destination> pois;
    private LocationProvider locationProvider;

    public AppViewModel() {
        this.apiModel = new APIModel(this, this);
        this.destinationModel = new DestinationModel();
        this.directionModel = new DirectionModel();
        this.pois = new ArrayList<>();
        this.subject = new Subject();
    }

    @Override
    public void setDestination(Destination destination) {
        destinationModel.setCurrentDestination(destination);
        apiModel.getRoute(locationProvider.getLastLocation(), destination.getGeoPoint());
    }

    public MutableLiveData<Destination> getDestination() {
        return destinationModel.getCurrentDestination();
    }

    public ArrayList<Destination> getLoadedPOIs() {
        return pois;
    }

    @Override
    public ArrayList<Destination> getDestinationList() {
        return destinationModel.getPastDestinations().getValue();
    }

    public void getPOIs(GeoPoint geoPoint) {

        apiModel.getPOIs(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    @Override
    public void fillPOIs(ArrayList<Destination> pois) {
        this.pois = pois;
        subject.notifyObservers();
    }


    @Override
    public void setRoute(Route route) {
        directionModel.setRoute(route);
    }

    @Override
    public ArrayList<GeoPoint> getRouteCoordinates() {
        ArrayList<GeoPoint> list = new ArrayList<>();

        for (Integer i : directionModel.getCoordinates().keySet()) {
            list.add(directionModel.getCoordinates().get(i));
        }


        return list;
    }

    @Override
    public ArrayList<Step> getRouteSteps() {
        return directionModel.getSteps();
    }

    public void setLocationProvider(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }
    public void onLocationChanged(GeoPoint lastLocation) {
    }
}
