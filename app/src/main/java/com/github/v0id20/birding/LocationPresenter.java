package com.github.v0id20.birding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

public class LocationPresenter implements LocationAdapter.OnMyLocationClickListener, LocationAdapter.OnChosenLocationClickListener {
    public static final int REQUEST_CODE = 231;
    private FusedLocationProviderClient fusedLocationClient;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;
    private ChooseLocationActivity view;

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
                showDialog();
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
                                i.putExtra("Latitude", latitude);
                                i.putExtra("Longitude", longitude);
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

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

// Setting Dialog Title
        alertDialog.setTitle("GPS settings");
// Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
// On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                ConstraintLayout cl = ((Activity) context).findViewById(R.id.constraint_layout);
                //move to view
                Snackbar snackbar = Snackbar.make(cl, R.string.error_cant_retrieve_obs_without_location_on, 5000);
                snackbar.show();
            }
        });
        alertDialog.show();
    }
}
