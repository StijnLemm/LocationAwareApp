package com.study.locationawareapp.ui.destination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.study.locationawareapp.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class StationSuggestionAdapter extends BaseAdapter implements Observer {

    private final DestinationListProvider destinationListProvider;
    private final LayoutInflater inflater;
    private ArrayList<Destination> stations;
    private ArrayList<Destination> searchedStations;

    public StationSuggestionAdapter(Context context, DestinationListProvider destinationListProvider) {
        this.destinationListProvider = destinationListProvider;
        this.stations = destinationListProvider.getDestinationList();
        this.inflater = LayoutInflater.from(context);
        searchedStations = new ArrayList<>();
    }

    /**
     * Filter used for searching stations
     *
     * @param input the text from the search view
     */
    public void filter(String input) {
        // Convert to lowercase for better findability
        input = input.toLowerCase();
        // Clear the last searched stations
        searchedStations.clear();

        // If there is nothing put in we don't show the list view
        if (!input.isEmpty()) {
            // For each station we check if the name contains the input string
            for (Destination destination : stations) {
                if (destination.getName().toLowerCase().contains(input)) {
                    searchedStations.add(destination);
                }
            }
        }

        // Notify the adapter that the list changed
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchedStations.size();
    }

    @Override
    public Destination getItem(int position) {
        return searchedStations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_station, null);
            holder.textViewStationName = (TextView) view.findViewById(R.id.TextView_item_station);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textViewStationName.setText(searchedStations.get(position).getName());
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.stations = destinationListProvider.getDestinationList();
    }

    class ViewHolder {
        TextView textViewStationName;
    }
}
