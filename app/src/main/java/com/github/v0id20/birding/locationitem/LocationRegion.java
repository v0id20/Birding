package com.github.v0id20.birding.locationitem;

public class LocationRegion extends LocationItem {
    private final LocationCountry country;

    public LocationRegion(String regionName, String regionCode, LocationCountry country) {
        super(regionName, regionCode);
        this.country = country;
    }

    public LocationCountry getCountry() {
        return country;
    }
}
