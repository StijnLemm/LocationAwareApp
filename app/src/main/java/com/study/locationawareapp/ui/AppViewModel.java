package com.study.locationawareapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.study.locationawareapp.ui.destination.Destination;
import com.study.locationawareapp.ui.destination.DestinationModel;
import com.study.locationawareapp.ui.destination.DestinationSetter;
import com.study.locationawareapp.ui.destination.ListProvider;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class AppViewModel extends ViewModel implements DestinationSetter, ListProvider, POIsHolder {
    public Subject subject;
    private final DestinationModel destinationModel;
    private final APIModel apiModel;
    private ArrayList<Destination> pois;

    public AppViewModel() {
        this.apiModel = new APIModel(this);
        this.destinationModel = new DestinationModel();
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



}
