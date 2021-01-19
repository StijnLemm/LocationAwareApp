package com.study.locationawareapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.study.locationawareapp.ui.api.APIModel;
import com.study.locationawareapp.ui.destination.Destination;
import com.study.locationawareapp.ui.destination.DestinationModel;
import com.study.locationawareapp.ui.destination.DestinationSetter;
import com.study.locationawareapp.ui.destination.ListProvider;
import com.study.locationawareapp.ui.directions.DirectionModel;
import com.study.locationawareapp.ui.directions.Route;
import com.study.locationawareapp.ui.directions.Step;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class AppViewModel extends ViewModel implements DestinationSetter, ListProvider, POIsHolder, RouteHolder {
    public Subject subject;
    private final DestinationModel destinationModel;
    private final DirectionModel directionModel;
    private final APIModel apiModel;
    private ArrayList<Destination> pois;

    public AppViewModel() {
        this.apiModel = new APIModel(this,this);
        this.destinationModel = new DestinationModel();
        this.directionModel = new DirectionModel();
        this.pois = new ArrayList<>();
        this.subject = new Subject();
    }

    @Override
    public void setDestination(Destination destination) {
        destinationModel.setCurrentDestination(destination);
    }

    public MutableLiveData<Destination> getDestination() {
        return destinationModel.getCurrentDestination();
    }

    public ArrayList<Destination> getLoadedPOIs() {
        return pois;
    }

    @Override
    public ArrayList<Destination> getList() {
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
        return directionModel.getCoordinates();
    }

    @Override
    public ArrayList<Step> getRouteSteps() {
        return directionModel.getSteps();
    }
}
