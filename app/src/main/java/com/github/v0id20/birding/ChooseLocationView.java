package com.github.v0id20.birding;

import android.location.LocationManager;

import java.util.ArrayList;

public interface ChooseLocationView {
    void displayCountriesListReceived(ArrayList<LocationCountry> locationDataArrayList);

    void displayRegionListReceived(ArrayList<LocationRegion> locationList, int position);

    void onChosenLocationClick(LocationRegion region);

    void onMyLocationClick(double lat, double lon);

    LocationManager getLocationManager();

    boolean checkLocationPermissionGranted();

    void requestLocationPermission();

    void showEnableGpsDialog();

    void showLocationDisabledToast();

    void showUnableToGetLocationToast();

    void setCountriesLoadingErrorState();

    void setRegionsLoadingErrorState(int position);

    void setLoadingState();

}
