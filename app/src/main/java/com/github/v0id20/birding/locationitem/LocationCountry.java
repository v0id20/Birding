package com.github.v0id20.birding.locationitem;

import java.util.ArrayList;
import java.util.Arrays;


public class LocationCountry extends LocationItem {
    private boolean expanded;
    private ArrayList<LocationRegion> subRegions = new ArrayList(Arrays.asList(new LocationRegion("All Regions", this.getLocationCode(), this)));

    public LocationCountry(String location) {
        super(location, null);
    }

    public LocationCountry(String location, String locationCode) {
        super(location, locationCode);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public ArrayList<LocationRegion> getSubRegions() {
        return subRegions;
    }

    public void setSubRegions(ArrayList<LocationRegion> subRegions) {
        this.subRegions = subRegions;
    }
}
