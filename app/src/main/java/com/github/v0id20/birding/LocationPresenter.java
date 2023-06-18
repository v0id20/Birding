package com.github.v0id20.birding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationPresenter implements LocationAdapter.OnMyLocationClickListener, LocationAdapter.OnChosenLocationClickListener {
    public static final int REQUEST_CODE = 231;
    private FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private LocationModel locationModel;

    public LocationPresenter(Context context) {
        super();
        this.context = context;
        locationModel = new LocationModel();
    }

    public void onCreatePresenter() {
        if (locationModel != null) {
            ((ChooseLocationActivity) context).showLocationsList(locationModel.getLocationList());
        }
    }


    @Override
    public void onChosenLocationClick(String locationCode, String countryName) {
        Intent i = new Intent(context, ViewObservationsListActivity.class);
        i.putExtra(BirdObservation.REGION_CODE_EXTRA, locationCode);
        i.putExtra(BirdObservation.COUNTRY_NAME_EXTRA, countryName);
        context.startActivity(i);
    }

    @Override
    public void onMyLocationClick() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (lm.isLocationEnabled()) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Intent i = new Intent(context, ViewObservationsListActivity.class);
                            i.putExtra(BirdObservation.LATITUDE_EXTRA, location.getLatitude());
                            i.putExtra(BirdObservation.LONGITUDE_EXTRA, location.getLongitude());
                            context.startActivity(i);
                        } else {
                            System.out.println("Location is null");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("failure occurred");
                    }
                });
            } else {
                //ask user to enable location
                ((ChooseLocationActivity) context).showDialog();
            }
        } else {
            requestLocationPermission();
        }
    }

    public void requestLocationPermission() {
        ((Activity) context).requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
    }

    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == LocationPresenter.REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude;
                                double longitude;
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Intent i = new Intent(context, ViewObservationsListActivity.class);
                                i.putExtra(BirdObservation.LATITUDE_EXTRA, latitude);
                                i.putExtra(BirdObservation.LONGITUDE_EXTRA, longitude);
                                context.startActivity(i);
                            }
                        }
                    });
                }

                // Other 'case' lines to check for other
                // permissions this app might request.
            }

        }
    }


}
