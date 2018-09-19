package com.example.user.forecast_weather;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import static android.location.LocationManager.*;

public class UserLocation {

    private FusedLocationProviderClient client;
    double latitude;
    double longitude;
    Context context;


    public UserLocation(Context context) {
        latitude = 0;
        longitude = 0;
        this.context = context;
        client = LocationServices.getFusedLocationProviderClient(context);
        getLocation();
    }

    public void getLocation() {
        Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;
        double lat;
        double lng;

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        System.out.println("inside :)");
        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(bestProvider);

        if (location == null) {
            Toast.makeText(context, "Location Not found", Toast.LENGTH_LONG).show();
        } else {
            geocoder = new Geocoder(context);
            try {
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                lat = (double) user.get(0).getLatitude();
                lng = (double) user.get(0).getLongitude();
                System.out.println(" DDD lat: " + lat + ",  longitude: " + lng);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Errorr! :"+e.getMessage());
            }
        }
    }
}
