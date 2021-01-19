package com.study.locationawareapp.ui.api;

import android.util.JsonReader;

import com.study.locationawareapp.ui.destination.Destination;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomJSONParser {
    public static ArrayList<Destination> POIParser(String data) {
        ArrayList<Destination> destinations = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray types = jsonObject.getJSONArray("payload");
            JSONObject stations = types.getJSONObject(0);
            JSONArray locations = stations.getJSONArray("locations");

            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);

                String name = location.getString("name");
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                Destination destination = new Destination(name,lat,lng);
                destinations.add(destination);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return destinations;
    }
}
