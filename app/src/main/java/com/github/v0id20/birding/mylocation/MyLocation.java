package com.github.v0id20.birding.mylocation;

public class MyLocation {
    private final String locationName;
    private final String locationCode;

    public MyLocation(String locationName, String locationCode) {
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


