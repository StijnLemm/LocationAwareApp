package com.study.locationawareapp.ui;

import android.util.Log;

import com.study.locationawareapp.ui.destination.Destination;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.study.locationawareapp.ui.CustomJSONParser.POIParser;

public class APIModel {
    private final POIsHolder poisHolder;
    private OkHttpClient client;
    private final String NS_BASE_URL = "https://gateway.apiportal.ns.nl/places-api/v2/";

    public APIModel(POIsHolder poisHolder) {
        this.client = new OkHttpClient();
        this.poisHolder = poisHolder;
    }

    public void getPOIs(double latitude, double longitude) {
        //TODO
        int radiusInMeters = 20000;
        String url = NS_BASE_URL+"places?lat="+latitude+"&lng="+longitude+"&radius="+radiusInMeters+"&type=stationV2";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Ocp-Apim-Subscription-Key","cb9a2ac92d124191b4216388587db953")
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ORSAPI", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.d("ORSAPI", " onResponse " + data);
                    ArrayList<Destination> destinations = POIParser(data);
                    poisHolder.fillPOIs(destinations);
                }
            }
        });
    }

    public void getPOIs(String name) {
        String url = NS_BASE_URL+"places?q="+name+"&type=stationV2";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Ocp-Apim-Subscription-Key","cb9a2ac92d124191b4216388587db953")
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("ORSAPI", "onFailure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.d("ORSAPI", " onResponse " + data);
                    ArrayList<Destination> destinations = POIParser(data);
                    //poisHolder.fillPOIs(destinations);
                }
            }
        });
    }



}
