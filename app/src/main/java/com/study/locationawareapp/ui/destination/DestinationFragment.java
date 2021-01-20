package com.study.locationawareapp.ui.destination;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.api.TravelProfile;

import static android.content.ContentValues.TAG;

public class DestinationFragment extends Fragment implements DestinationSetter {

    private ConstraintLayout placeholderCurrent;
    private AppViewModel appViewModel;
    private TextView textCurrent;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_destination, container, false);
        this.appViewModel = new ViewModelProvider(this.getActivity()).get(AppViewModel.class);
        final RecyclerView recyclerView = root.findViewById(R.id.RecyclerView_lastDestinations);

        //todo search field
        final SearchView searchView = root.findViewById(R.id.SearchView_destination);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Bind the refresh button to the refresh method
        ImageView refreshButton = root.findViewById(R.id.ImageView_refreshButton);
        refreshButton.setOnClickListener(view -> refreshRecentLocations());

        // List of all the travel profiles, wheelchair is not in here because the api doesn't work well with it
        TravelProfile[] travelProfiles = new TravelProfile[]{TravelProfile.walking, TravelProfile.cycling , TravelProfile.car};
        // Spinner logic
        Spinner spinner = root.findViewById(R.id.Spinner_destination_transportType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appViewModel.setTravelProfile(travelProfiles[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Creating the ArrayAdapter instance having the possible travel profiles
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, travelProfiles);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        // If the destination changes in the model we set the destination in the ui
        appViewModel.getDestination().observe(getViewLifecycleOwner(), this::setDestination);

        // Finding the views used in the current destination
        placeholderCurrent = root.findViewById(R.id.ConstraintLayout_destination_current);
        textCurrent = root.findViewById(R.id.TextView_destination_current);
        // Inflate the current into the constraint layout
        getLayoutInflater().inflate(R.layout.item_destination, placeholderCurrent);
        // Update to the current set destination
        setDestination(appViewModel.getDestination().getValue());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DestinationAdapter(appViewModel, appViewModel));

        return root;
    }

    /***
     * This method is designed to change the current destination, also the destination can be not set
     * @param destination current destination, can be null
     */
    public void setDestination(Destination destination) {
        if (destination == null) {
            placeholderCurrent.setVisibility(View.GONE);
            textCurrent.setVisibility(View.GONE);
        } else {
            placeholderCurrent.setVisibility(View.VISIBLE);
            textCurrent.setVisibility(View.VISIBLE);

            //fill all the fields
            TextView title = placeholderCurrent.findViewById(R.id.TextView_destinationItem_title);
            title.setText(destination.getName());

            TextView duration = placeholderCurrent.findViewById(R.id.TextView_destinationItem_duration);
            duration.setText("" + destination.getDurationInMinutes());

            TextView switches = placeholderCurrent.findViewById(R.id.TextView_destinationItem_switches);
            switches.setText("" + destination.getSwitches());
        }
    }

    public void refreshRecentLocations() {
        //todo add refresh logic
    }

}