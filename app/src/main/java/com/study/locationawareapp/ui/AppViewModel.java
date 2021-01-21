package com.study.locationawareapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.study.locationawareapp.ui.api.APIModel;
import com.study.locationawareapp.ui.api.TravelProfile;
import com.study.locationawareapp.ui.destination.CurrentDestinationHolder;
import com.study.locationawareapp.ui.destination.Destination;
import com.study.locationawareapp.ui.destination.DestinationModel;
import com.study.locationawareapp.ui.destination.DestinationSetter;
import com.study.locationawareapp.ui.destination.DestinationListProvider;
import com.study.locationawareapp.ui.destination.PreviousDestinationsListProvider;
import com.study.locationawareapp.ui.directions.DirectionModel;
import com.study.locationawareapp.ui.directions.DirectionsListProvider;
import com.study.locationawareapp.ui.directions.Route;
import com.study.locationawareapp.ui.directions.Step;
import com.study.locationawareapp.ui.map.LocationProvider;
import com.study.locationawareapp.ui.map.TrainRouteView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AppViewModel extends ViewModel implements DestinationSetter, DestinationListProvider, POIsHolder, RouteHolder, DirectionsListProvider, PreviousDestinationsListProvider {
    public Subject poiChangedSubject;
    public Subject routeChangedSubject;
    public Subject previousPOIsChangedSubject;
    private final DestinationModel destinationModel;
    private final DirectionModel directionModel;
    private final APIModel apiModel;
    private ArrayList<Destination> pois;
    private ArrayList<Destination> previousPois;
    private LocationProvider locationProvider;
    private TrainRouteView trainRouteView;
    private GeoPoint lastLocation;

    public AppViewModel() {
        this.apiModel = new APIModel(this, this);
        this.destinationModel = new DestinationModel();
        this.directionModel = new DirectionModel();
        this.pois = new ArrayList<>();
        this.previousPois = new ArrayList<>();
        this.poiChangedSubject = new Subject(0);
        this.routeChangedSubject = new Subject(1);
        this.previousPOIsChangedSubject = new Subject(2);
    }

    @Override
    public void setDestination(Destination destination) {
        destinationModel.setCurrentDestination(destination);
        Destination closestStation = this.getClosestStation(this.lastLocation);
        apiModel.getRoute(locationProvider.getLastLocation(), closestStation.getGeoPoint());
        apiModel.getNSInformation(closestStation.getUICCode(), destination.getUICCode());
        addPreviousPOI(destination);
    }

    public void setTrainRouteView(TrainRouteView trainRouteView) {
        this.trainRouteView = trainRouteView;
    }

    public MutableLiveData<Destination> getDestination() {
        return destinationModel.getCurrentDestination();
    }

    @Override
    public ArrayList<Destination> getDestinationList() {
        return pois;
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
    public void setTrainRoute(Polyline route) {
        if (this.trainRouteView != null)
            this.trainRouteView.drawTrainRoute(route);
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
        this.lastLocation = lastLocation;
    }

    public void setTravelProfile(TravelProfile travelProfile) {
        apiModel.setTravelProfile(travelProfile);
    }

    public void setPOIs(List<Destination> pois) {
        this.pois = new ArrayList<>(pois);
        poiChangedSubject.notifyObservers();
    }

    public void addPreviousPOI(Destination destination) {
        previousPois.remove(destination);


        this.previousPois.add(destination);
        previousPOIsChangedSubject.notifyObservers();
    }

    public void setPreviousPOIs(ArrayList<Destination> previousPOIs) {
        this.previousPois = previousPOIs;
        previousPOIsChangedSubject.notifyObservers();
    }

    @Override
    public ArrayList<Destination> getPreviousPOIsList() {
        ArrayList<Destination> destinations = new ArrayList<>(previousPois);
        Collections.reverse(destinations);
        return destinations;
    }

    public Destination getClosestStation(GeoPoint currentLocation) {
        double smallestDistance = Double.MAX_VALUE;

        Destination closestStation = new Destination("No stations found", 0, 0, 0);

        for (Destination destination : pois) {
            GeoPoint geoPoint = new GeoPoint(destination.getLatitude(), destination.getLongitude());
            double distance = geoPoint.distanceToAsDouble(currentLocation);
            if (smallestDistance > distance) {
                closestStation = destination;
                smallestDistance = distance;
            }
        }

        return closestStation;

    }


    public void setCurrentDestinationHolder(CurrentDestinationHolder currentDestinationHolder) {
        this.apiModel.setCurrentDestinationHolder(currentDestinationHolder);
    }


}
