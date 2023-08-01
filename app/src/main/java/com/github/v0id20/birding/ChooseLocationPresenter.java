package com.github.v0id20.birding;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class ChooseLocationPresenter implements ChooseLocationAdapter.OnMyLocationClickListener, ChooseLocationAdapter.OnRegionClickListener, ChooseLocationAdapter.OnCountryClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private final ChooseLocationView chooseLocationView;
    private final ChooseLocationModel chooseLocationModel;

    public ChooseLocationPresenter(ChooseLocationView chooseLocationView) {
        this.chooseLocationView = chooseLocationView;
        chooseLocationModel = new ChooseLocationModel();
    }

    public void requestLocationData() {
        chooseLocationModel.requestCountriesList(locationList -> onCountriesListReceived(locationList));
    }

    private void onCountriesListReceived(ArrayList<LocationCountry> locationList) {
        chooseLocationView.displayCountriesListReceived(locationList);
    }

    private void onRegionListReceived(ArrayList<LocationRegion> locationList, int position) {
        chooseLocationView.displayRegionListReceived(locationList, position);
    }

    @SuppressLint("MissingPermission")
    public void onRequestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == ChooseLocationActivity.REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted.
                getCurrentLocation(chooseLocationView.getLocationManager().isLocationEnabled());
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                //Permission denied
                chooseLocationView.showLocationDisabledToast();
            }
        }
    }

    @Override
    public void onMyLocationClick() {
        if (chooseLocationView.checkLocationPermissionGranted()) {
            getCurrentLocation(chooseLocationView.getLocationManager().isLocationEnabled());
        } else {
            chooseLocationView.requestLocationPermission();
        }
    }

    @Override
    public void onRegionClick(LocationRegion region) {
        chooseLocationView.onChosenLocationClick(region);
    }

    public void onCountryClick(LocationCountry country, int position) {
        ChooseLocationModel.OnRequestRegionResultCallback khh = new ChooseLocationModel.OnRequestRegionResultCallback() {
            @Override
            public void onRequestRegionResult(ArrayList<LocationRegion> locationList) {
                onRegionListReceived(locationList, position);
            }
        };
        chooseLocationModel.requestSubnationalRegionsList(country, khh);
    }

    public void getCurrentLocation(boolean locationEnabled) {
        if (locationEnabled) {
            getLastOrCurrentLocation();
        } else {
            chooseLocationView.showEnableGpsDialog();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLastOrCurrentLocation() {
        fusedLocationClient = chooseLocationView.getFusedLocationProviderClient();
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    chooseLocationView.onMyLocationClick(location.getLatitude(), location.getLongitude());
                } else {
                    OnRetrieveLocationListener currentLocationListener = new OnRetrieveLocationListener();
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(currentLocationListener);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("failure occurred");
            }
        });
    }

    public class OnRetrieveLocationListener implements OnFailureListener, OnSuccessListener<Location> {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.e("ChooseLocationPresenter", "onFailure: could not  retrieve current location", e);
            chooseLocationView.showUnableToGetLocationToast();
        }

        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                chooseLocationView.onMyLocationClick(location.getLatitude(), location.getLongitude());
            }
        }
    }

}