package com.example.user.forecast_weather;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class UserLocation2 implements LocationListener {

    private LocationManager locationManager;
    Context context;
    String provider;
    double latitude, longitude;


    public UserLocation2(Context context) {
        latitude = 0;
        longitude = 0;
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},1000);
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,},1000);
        }else{
            System.out.println("hi: ");

        }
        locationManager.requestLocationUpdates(provider, 3000, 3, this);
        onLocationChanged(locationManager.getLastKnownLocation(provider));



    }


        @Override
        public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

}
