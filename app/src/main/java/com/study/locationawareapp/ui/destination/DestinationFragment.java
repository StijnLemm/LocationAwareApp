package com.study.locationawareapp.ui.destination;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.locationawareapp.R;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.api.TravelProfile;

import static android.content.ContentValues.TAG;

public class DestinationFragment extends Fragment implements DestinationSetter, CurrentDestinationHolder {

    private ConstraintLayout placeholderCurrent;
    private AppViewModel appViewModel;
    private TextView textCurrent;
    private StationSuggestionAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_destination, container, false);
        this.appViewModel = new ViewModelProvider(this.getActivity()).get(AppViewModel.class);

        // Recycler view logic
        RecyclerView recyclerView = root.findViewById(R.id.RecyclerView_lastDestinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DestinationAdapter destinationAdapter = new DestinationAdapter(appViewModel, appViewModel);
        recyclerView.setAdapter(destinationAdapter);
        appViewModel.previousPOIsChangedSubject.attachObserver(((o, arg) -> {
            if (getActivity() != null)
                getActivity().runOnUiThread(()->{
                    destinationAdapter.notifyDataSetChanged();
                });
        }));

        // Logic behind the search view
        SearchView searchView = root.findViewById(R.id.SearchView_destination);
        ListView listView = root.findViewById(R.id.ListView_destination_suggestions);

        // Create the custom adapter
        this.adapter = new StationSuggestionAdapter(getContext(), appViewModel);
        appViewModel.poiChangedSubject.attachObserver(adapter);

        // Set adapter
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Set the current destination
            appViewModel.setDestination(adapter.getItem(position));
            // Clear focus so the keyboard doesn't show
            searchView.clearFocus();
            // Clear the search bar and this will clear the list view
            searchView.setQuery("", true);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "SearchView submitted: " + query);

                if (adapter.getCount() > 0) {

                    // Get the top from list view
                    Destination destination = adapter.getItem(0);

                    // Clear focus so the keyboard doesn't show
                    searchView.clearFocus();
                    // Clear the search bar and this will clear the list view
                    searchView.setQuery("", true);

                    appViewModel.setDestination(destination);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String input) {
                adapter.filter(input);
                Log.i(TAG, "onQueryTextChange: " + input);
                return false;
            }
        });

        // List of all the travel profiles, wheelchair is not in here because the api doesn't work well with it
        TravelProfile[] travelProfiles = new TravelProfile[]{TravelProfile.walking, TravelProfile.cycling, TravelProfile.car};
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
        appViewModel.setCurrentDestinationHolder(this);

        // Finding the views used in the current destination
        placeholderCurrent = root.findViewById(R.id.ConstraintLayout_destination_current);
        this.placeholderCurrent.setOnClickListener(this::getNSWebView);
        textCurrent = root.findViewById(R.id.TextView_destination_current);

        // Inflate the current into the constraint layout
        getLayoutInflater().inflate(R.layout.item_destination, placeholderCurrent);
        // Update to the current set destination
        setDestination(appViewModel.getDestination().getValue());

        return root;
    }

    private String currentURLContent;

    public void getNSWebView(View view) {
        if (currentURLContent != null) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.blue));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this.getContext(), Uri.parse(this.currentURLContent));
        }
    }

    @Override
    public void setURLContent(String url) {
        this.currentURLContent = url;
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
        }
    }
}