package com.github.v0id20.birding;

import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

public interface ChooseLocationView {
    void displayCountriesListReceived(ArrayList<LocationCountry> locationDataArrayList);

    void onChosenLocationClick(LocationRegion region);

    void onMyLocationClick(double lat, double lon);

    LocationManager getLocationManager();

    FusedLocationProviderClient getFusedLocationProviderClient();

    boolean checkLocationPermissionGranted();

    void requestLocationPermission();

    void showEnableGpsDialog();

    void showLocationDisabledToast();

    void showUnableToGetLocationToast();

    void displayRegionListReceived(ArrayList<LocationRegion> locationList, int position);

}
