package com.github.v0id20.birding.mylocation;

public class LocationRegion extends MyLocation {
    private final LocationCountry country;

    public LocationRegion(String regionName, String regionCode, LocationCountry country) {
        super(regionName, regionCode);
        this.country = country;
    }

    public LocationCountry getCountry() {
        return country;
    }
}
