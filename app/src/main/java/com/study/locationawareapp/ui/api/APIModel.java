package com.study.locationawareapp.ui.api;

import android.util.Log;

import com.study.locationawareapp.ui.POIsHolder;
import com.study.locationawareapp.ui.destination.Destination;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.study.locationawareapp.ui.api.CustomJSONParser.POIParser;

public class APIModel {
    private final POIsHolder poisHolder;
    private OkHttpClient client;
    private final String NS_BASE_URL = "https://gateway.apiportal.ns.nl/places-api/v2/";
    private final String ORS_BASE_URL = "https://api.openrouteservice.org/v2/directions/";

    public APIModel(POIsHolder poisHolder) {
        this.client = new OkHttpClient();
        this.poisHolder = poisHolder;
    }

    public void getPOIs(double latitude, double longitude) {
        //TODO fine tune the radius
        int radiusInMeters = 20000;
        String url = NS_BASE_URL + "places?lat=" + latitude + "&lng=" + longitude + "&radius=" + radiusInMeters + "&type=stationV2";

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

    public void getRoute(GeoPoint start, GeoPoint end) {

        String url = constructUrlPostMethod(TravelProfile.walking, start, end);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ORS-API", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.d("ORS-API", " onResponse " + data);
                    //todo do something with the route
                }
            }
        });
    }

    private String constructUrlPostMethod(TravelProfile travelProfile, GeoPoint start, GeoPoint end) {
        // Get the base url
        String result = ORS_BASE_URL;

        // Add profile
        result += travelProfile.label + "?";

        // Add the api key
        result += "api_key=" + "5b3ce3597851110001cf62485b882f57ae2f4fc08dc8070ae9f59e79";

        // Add the starting position
        result += "&start=" + start.getLatitude() + "," + start.getLongitude();

        // Add the ending position
        result += "&end=" + end.getLatitude() + "," + end.getLongitude();

        return result;
    }


}
