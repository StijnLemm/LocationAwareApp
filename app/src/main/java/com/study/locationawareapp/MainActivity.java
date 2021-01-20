package com.study.locationawareapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.study.locationawareapp.ui.AppViewModel;
import com.study.locationawareapp.ui.CustomViewPager;
import com.study.locationawareapp.ui.map.MapViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static final long LISTENER_INTERVAL = 5000;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        });
    }

    private void init() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        CustomViewPager viewPager = findViewById(R.id.ViewPager_main);
        viewPager.disableScroll(true);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.selectTab(tabs.getTabAt(1));


        this.startLocationListener();
    }

    private void startLocationListener(){
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        AppViewModel appViewModel =
                new ViewModelProvider(this).get(AppViewModel.class);
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    appViewModel.onLocationChanged(mapViewModel.getLastLocation());
                } catch (Exception e){
                    Log.d("Timer", "run: location timer", e);
                }
            }
        },LISTENER_INTERVAL, LISTENER_INTERVAL);
    }

    private void requestPermissionsIfNecessary(String[] strings) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : strings) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);

            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    PERMISSIONS_REQUEST_CODE);
        } else {
            this.init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_CODE){
            boolean init = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    init = true;
                } else {
                    break;
                }
            }
            if(init){
                this.init();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}