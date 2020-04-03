package com.example.ex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    TextView textView;
    double RT = 6371000;
    double lat = 0;
    double lon = 0;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        assert location != null;
        setLat(location.getLatitude());
        setLon(location.getLongitude());
        textView.setText("latitude : " + getLat() + "\nlongitude : " + getLon());

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double newLat = location.getLatitude();
                double newLon = location.getLongitude();

                double latRad1 = convertRad(getLat());
                double lonRad1 = convertRad(getLat());
                double latRad2 = convertRad(newLat);
                double lonRad2 = convertRad(newLon);

                double d = RT * (Math.PI/2 - Math.asin( Math.sin(newLat) * Math.sin(getLat()) + Math.cos(newLon - getLon()) * Math.cos(newLat) * Math.cos(getLat())));


                setLat(newLat);
                setLon(newLon);

                textView.setText("latitude : " + getLat() + "\nlongitude : " + getLon() + "\ndistance parcouru : " + d);
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
        });
    }

    private double convertRad(double deg) {
        return (Math.PI * deg)/180;
    }
}
