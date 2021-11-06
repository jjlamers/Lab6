package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.webkit.PermissionRequest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDestinationLatLng = new LatLng(43.0753796,-89.4064015);
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapFragment.getMapAsync( googleMap -> {
            mMap = googleMap;

            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Destination")
            );
            displayMyLocation();

            addCurLocationMarker(googleMap);
        });

    }

    private void displayMyLocation(){
        int permission = ActivityCompat.checkSelfPermission(
                this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        );

        if(permission== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        } else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this,task -> {
                        Location mLastKnownLocation = task.getResult();

                        if (task.isSuccessful() && mLastKnownLocation != null){
                            mMap.addPolyline(
                                    new PolylineOptions().add(
                                            new LatLng(
                                                    mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()),
                                            mDestinationLatLng)
                            );
                        }

                    });
        }
    }

    private void addCurLocationMarker(GoogleMap googleMap){
        int permission = ActivityCompat.checkSelfPermission(
                this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        );

        if(permission== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        } else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this,task -> {
                        Location mLastKnownLocation = task.getResult();

                        if (task.isSuccessful() && mLastKnownLocation != null){
                            mMap.addMarker(new MarkerOptions()
                                    .position(
                                            new LatLng(
                                                mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()))
                                    .title("Current Location")
                            );
                        }

                    });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                            @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                displayMyLocation();
            }
        }
    }
}