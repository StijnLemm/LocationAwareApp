package com.study.locationawareapp.ui;

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

public class AppViewModel extends ViewModel implements DestinationSetter, DestinationListProvider, POIsHolder, RouteHolder, DirectionsListProvider {
    public Subject poiChangedSubject;
    public Subject routeChangedSubject;
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
        this.poiChangedSubject = new Subject(0);
        this.routeChangedSubject = new Subject(1);
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
        poiChangedSubject.notifyObservers();
    }


    @Override
    public void setRoute(Route route) {
        directionModel.setRoute(route);
        routeChangedSubject.notifyObservers();
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
        if (directionModel.getRoute() != null) {
            boolean routeChanged = directionModel.getRoute().hasVisitedGeoPoint(lastLocation);
            if (routeChanged)
                routeChangedSubject.notifyObservers();

            double distanceToClosestCoordinate = directionModel.getRoute().distanceToClosestCoordinte(lastLocation);
            boolean farFromRoute = distanceToClosestCoordinate > 25;
            if (farFromRoute)
                setDestination(getDestination().getValue());
        }
    }

}
