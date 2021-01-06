package com.study.locationawareapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.study.locationawareapp.services.LocationForegroundService;

public class App extends Application {
    public static final String CHANNEL_ID = "GPS_SERVICE_CHANNEL";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    LocationForegroundService.NOTIFY_CHANNEL_NAME,
                    // at least LOW, here we use DEFAULT
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
