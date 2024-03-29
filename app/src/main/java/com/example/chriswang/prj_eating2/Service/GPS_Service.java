package com.example.chriswang.prj_eating2.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.net.ConnectException;

/**
 * Created by ChrisWang on 2017/11/10.
 */

public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLocation(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 3000, 0, listener);
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, listener);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //使用GPS定位座標
        }
        else if ( locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        getLocation(location);
    }

    public void getLocation(Location location){

        if(location != null) {
            Intent i = new Intent("location_update");
            i.putExtra("long", location.getLongitude());
            i.putExtra("lat", location.getLatitude());
            sendBroadcast(i);
        }
        else {
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null) {
            locationManager.removeUpdates(listener);
        }
    }

}


