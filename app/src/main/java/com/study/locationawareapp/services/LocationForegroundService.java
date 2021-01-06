package com.study.locationawareapp.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.study.locationawareapp.App;
import com.study.locationawareapp.MainActivity;
import com.study.locationawareapp.R;

public class LocationForegroundService extends Service {

    public static final String TAG = LocationForegroundService.class.getSimpleName();
    public static final String GPS_SERVICE_INTENT_ACTION = "GPS_ACTION_INTENT";
    public static final String GPS_LATITUDE_INTENT_KEY = "GPS_LATITUDE";
    public static final String GPS_LONGITUDE_INTENT_KEY = "GPS_LONGITUDE";
    public static final String NOTIFY_CHANNEL_NAME = "GPS_NOTIFY";
    private static final int LOCATION_INTERVAL = 1000; //TODO fine tune
    private static final float LOCATION_DISTANCE = 10f; //TODO fine tune

    private LocationListener locationListener;
    private LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServiceNotification();

        this.locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.currentDirectionString), Toast.LENGTH_LONG).show();
        }

        this.locationListener = new LocationListener(LocationManager.GPS_PROVIDER);

        try {
            this.locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    this.locationListener
            );
        } catch (SecurityException e){
            // This service will only start if permission is granted.
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        this.locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }

    // this method will init the foreground service notification (mandatory)
    private void startServiceNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("STARTUP", 1);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification serviceNotification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                //TODO string resources
                .setContentTitle("GPS service")
                .setContentText("Tap to open app")
                .setSmallIcon(R.drawable.ic_baseline_place_24)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, serviceNotification);
    }

    private class LocationListener implements android.location.LocationListener {

        // This Location is the most recent location, this object is changed whenever the onLocationChanged is called.
        private final Location lastLocation;

        public LocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            lastLocation.set(location);
            Intent intent = new Intent();

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            intent.setAction(GPS_SERVICE_INTENT_ACTION);
            intent.putExtra("GPS_LONGITUDE", longitude);
            intent.putExtra("GPS_LATITUDE", latitude);

            sendBroadcast(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}