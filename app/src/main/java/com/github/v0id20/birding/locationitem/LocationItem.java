package com.github.v0id20.birding.locationitem;

public class LocationItem {
    private final String locationName;
    private final String locationCode;

    public LocationItem(String locationName, String locationCode) {
        this.locationName = locationName;
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }
}


