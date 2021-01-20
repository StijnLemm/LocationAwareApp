package com.study.locationawareapp.ui.api;

import android.util.Log;

import com.study.locationawareapp.ui.POIsHolder;
import com.study.locationawareapp.ui.RouteHolder;
import com.study.locationawareapp.ui.destination.CurrentDestinationHolder;
import com.study.locationawareapp.ui.destination.Destination;
import com.study.locationawareapp.ui.directions.Route;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.study.locationawareapp.ui.api.CustomJSONParser.GetURLFromNSMessage;
import static com.study.locationawareapp.ui.api.CustomJSONParser.NSRouteParser;
import static com.study.locationawareapp.ui.api.CustomJSONParser.POIParser;
import static com.study.locationawareapp.ui.api.CustomJSONParser.RouteParser;

public class APIModel {
    private final POIsHolder poisHolder;
    private final RouteHolder routeHolder;
    private CurrentDestinationHolder currentDestinationHolder;
    private TravelProfile travelProfile;
    private OkHttpClient client;
    private final String NS_BASE_URL = "https://gateway.apiportal.ns.nl/places-api/v2/";
    private final String ORS_BASE_URL = "https://api.openrouteservice.org/v2/directions/";
    private final String NS_BASE_URL_INFO = "https://gateway.apiportal.ns.nl/reisinformatie-api/api/v3/trips";

    public APIModel(POIsHolder poisHolder, RouteHolder routeHolder) {
        this.client = new OkHttpClient();
        this.poisHolder = poisHolder;
        this.routeHolder = routeHolder;
        this.travelProfile = TravelProfile.walking;
    }

    public void getPOIs(double latitude, double longitude) {
        //TODO fine tune the radius
        //int radiusInMeters = 100000;
        String url = NS_BASE_URL + "places?type=stationV2";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Ocp-Apim-Subscription-Key", "cb9a2ac92d124191b4216388587db953")
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("NS-API", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.d("NS-API", " onResponse " + data);
                    ArrayList<Destination> destinations = POIParser(data);
                    poisHolder.fillPOIs(destinations);
                }
            }
        });
    }

    public void getNSInformation(String fromStation, String toStation){
        String url = constructUrlPostMethod(travelProfile);

        String body = "?fromStation=" + fromStation + "&toStation=" + toStation;

        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ORS-API", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.d("ORS-API", " onResponse " + data);
                if (response.isSuccessful()) {
                    String url = GetURLFromNSMessage(data);
                    Polyline route = NSRouteParser(data);
                    routeHolder.setTrainRoute(route);
                    currentDestinationHolder.setURLContent(url);
                }
            }
        });
    }

    public void getRoute(GeoPoint start,GeoPoint end) {

        String url = constructUrlPostMethod(travelProfile);;

        String body = "{\"coordinates\":[[" + start.getLongitude()+","+start.getLatitude()+"],["+end.getLongitude()+","+end.getLatitude() + "]]}";

        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ORS-API", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.d("ORS-API", " onResponse " + data);
                if (response.isSuccessful()) {
                    Route route = RouteParser(data);
                    routeHolder.setRoute(route);
                }
            }
        });
    }

    private String constructUrlPostMethod(TravelProfile travelProfile) {
        // Get the base url
        String result = ORS_BASE_URL;

        // Add profile
        result += travelProfile.label + "/geojson?";

        // Add the api key
        result += "api_key=" + "5b3ce3597851110001cf62485b882f57ae2f4fc08dc8070ae9f59e79";

        return result;
    }

    public void setTravelProfile(TravelProfile travelProfile) {
        this.travelProfile = travelProfile;
    }

    public void setCurrentDestinationHolder(CurrentDestinationHolder currentDestinationHolder) {
        this.currentDestinationHolder = currentDestinationHolder;
    }
}
