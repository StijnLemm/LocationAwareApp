package com.study.locationawareapp.ui.destination;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;

public class DestinationFragment extends Fragment {

    private DestinationViewModel destinationViewModel;
    private ConstraintLayout placeholderCurrent;
    private AppViewModel appViewModel;
    private TextView textCurrent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        destinationViewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_destination, container, false);
        this.appViewModel = new ViewModelProvider(this.getActivity()).get(AppViewModel.class);
        final RecyclerView recyclerView = root.findViewById(R.id.ListView_lastDestinations);

        //todo add binding to make suggestions and to set the current destination
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



        // Finding the views used in the current destination
        placeholderCurrent = root.findViewById(R.id.ConstraintLayout_destination_current);
        textCurrent = root.findViewById(R.id.TextView_destination_current);
        // Inflate the current into the constraint layout
        getLayoutInflater().inflate(R.layout.item_destination, placeholderCurrent);
        // Update to the current set destination
        updateCurrentDestination(null);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DestinationAdapter(destinationViewModel));

        return root;
    }

    /***
     * This method is designed to change the current destination, also the destination can be not set
     * @param destination current destination, can be null
     */
    public void updateCurrentDestination(Destination destination) {
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
}