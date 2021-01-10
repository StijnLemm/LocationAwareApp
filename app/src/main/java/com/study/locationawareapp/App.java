package com.study.locationawareapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "GPS_SERVICE_CHANNEL";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
