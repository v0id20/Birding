package com.github.v0id20.birding;

import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public interface ChooseLocationView {
    void displayLocationDataReceived(ArrayList<LocationViewType> locationDataArrayList);

    void onChosenLocationClick(String regionCode, String countryName);

    void onMyLocationClick(double lat, double lon);

    LocationManager getLocationManager();

    FusedLocationProviderClient getFusedLocationProviderClient();

    boolean checkLocationPermissionGranted();

    void requestLocationPermission();

    void showEnableGpsDialog();

    void showLocationDisabledToast();

    void showUnableToGetLocationToast();
}
