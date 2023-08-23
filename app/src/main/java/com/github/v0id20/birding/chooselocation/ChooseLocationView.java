package com.github.v0id20.birding.chooselocation;

import com.github.v0id20.birding.locationitem.LocationCountry;
import com.github.v0id20.birding.locationitem.LocationRegion;

import java.util.ArrayList;

public interface ChooseLocationView {
    void displayCountriesListReceived(ArrayList<LocationCountry> locationDataArrayList);

    void displayRegionListReceived(ArrayList<LocationRegion> locationList, int position);

    void openRegionObservations(LocationRegion region);

    void openCurrentLocationObservations(double lat, double lon);

    boolean checkLocationPermissionGranted();

    void requestLocationPermission();

    void showEnableGpsDialog();

    void showLocationDisabledToast();

    void showUnableToGetLocationToast();

    void setCountriesLoadingErrorState();

    void setRegionsLoadingErrorState(int position);

    void setLoadingState();

}
