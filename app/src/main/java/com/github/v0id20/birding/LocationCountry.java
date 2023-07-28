package com.github.v0id20.birding;

import java.util.ArrayList;

public class LocationCountry extends MyLocation {
    private int viewType;
    private boolean expanded;
    private ArrayList<LocationRegion> subRegions;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }


    public LocationCountry(int viewType, String location) {
        super(location, null);
        this.viewType = viewType;
    }

    public LocationCountry(int viewType, String location, String locationCode) {
        super(location, locationCode);
        this.viewType = viewType;
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
