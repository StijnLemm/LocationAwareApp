package com.study.locationawareapp.ui.api;

import android.util.JsonReader;

import com.study.locationawareapp.ui.destination.Destination;
import com.study.locationawareapp.ui.directions.Route;
import com.study.locationawareapp.ui.directions.Step;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashMap;

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

                Destination destination = new Destination(name, lat, lng);
                destinations.add(destination);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return destinations;
    }

    public static Route RouteParser(String data) {
        HashMap<Integer,GeoPoint> coordinates = routeParserCoordinates(data);
        ArrayList<Step> steps = routeParserSteps(data);
        double distance = routeParserDistance(data);
        int duration = routeParserDuration(data);

        return new Route(coordinates, steps, distance, duration);
    }

    public static Polyline NSRouteParser(String data){
        Polyline route = new Polyline();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("trips");
            JSONObject trip = jsonArray.getJSONObject(0);
            JSONArray legs = trip.getJSONArray("legs");
            for (int i = 0; i < legs.length(); i++) {
                JSONObject leg = legs.getJSONObject(i);
                JSONArray stops = leg.getJSONArray("stops");
                for (int j = 0; j < stops.length(); j++) {
                    JSONObject stop = stops.getJSONObject(j);
                    double lng = stop.getDouble("lng");
                    double lat = stop.getDouble("lat");
                    route.addPoint(new GeoPoint(lat, lng));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return route;
    }

    public static String GetURLFromNSMessage(String data){
        String url = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("trips");
            JSONObject trip = jsonArray.getJSONObject(0);
            JSONObject shareURL = trip.getJSONObject("shareUrl");
            url = shareURL.getString("uri");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static double routeParserDistance(String data) {
        double distance = 0;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray features = jsonObject.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            JSONArray segments = properties.getJSONArray("segments");
            JSONObject segment = segments.getJSONObject(0);

            distance = segment.getDouble("distance");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return distance;
    }

    private static int routeParserDuration(String data) {
        int duration = 0;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray features = jsonObject.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            JSONArray segments = properties.getJSONArray("segments");
            JSONObject segment = segments.getJSONObject(0);

            duration = segment.getInt("duration");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return duration;
    }

    private static HashMap<Integer,GeoPoint> routeParserCoordinates(String data) {
        HashMap<Integer,GeoPoint> coordinates = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray features = jsonObject.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONArray coordinatesJson = geometry.getJSONArray("coordinates");

            for (int i = 0; i < coordinatesJson.length(); i++) {
                JSONArray location = coordinatesJson.getJSONArray(i);

                double lng = location.getDouble(0);
                double lat = location.getDouble(1);

                GeoPoint geoPoint = new GeoPoint(lat, lng);
                coordinates.put(i,geoPoint);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    private static ArrayList<Step> routeParserSteps(String data) {
        ArrayList<Step> steps = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray features = jsonObject.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            JSONArray segments = properties.getJSONArray("segments");
            JSONObject segment = segments.getJSONObject(0);
            JSONArray stepsJson = segment.getJSONArray("steps");

            for (int i = 0; i < stepsJson.length(); i++) {
                JSONObject stepJson = stepsJson.getJSONObject(i);

                double distance = stepJson.getDouble("distance");
                int duration = stepJson.getInt("duration");
                int type = stepJson.getInt("type");
                String instruction = stepJson.getString("instruction");
                String streetName = stepJson.getString("name");
                int startWayPoint = stepJson.getJSONArray("way_points").getInt(0);
                int endWayPoint = stepJson.getJSONArray("way_points").getInt(1);

                Step step = new Step(distance, duration, type, instruction, streetName, startWayPoint, endWayPoint);
                steps.add(step);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return steps;
    }

    public static Destination StationParser(String data){
       Destination destinations = new Destination("No station in 25 km radius",0,0);

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray payload = jsonObject.getJSONArray("payload");
            JSONObject stations = payload.getJSONObject(0);
            JSONArray locations = stations.getJSONArray("locations");
            if (locations.length()>0){
                JSONObject station = locations.getJSONObject(0);

                String name = station.getString("name");
                double lat = station.getDouble("lat");
                double lng = station.getDouble("lng");

                destinations = new Destination(name,lat,lng);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return destinations;
    }
}
