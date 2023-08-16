package com.github.v0id20.birding.mylocation;

import java.util.ArrayList;
import java.util.Arrays;


public class LocationCountry extends MyLocation {
    public enum LoadingState {ERROR, SUCCESS, LOADING, VOID}

    private LoadingState loadingState;
    private boolean expanded;
    private ArrayList<LocationRegion> subRegions = new ArrayList(Arrays.asList(new LocationRegion("All Regions", this.getLocationCode(), this)));

    public LocationCountry(String location) {
        super(location, null);
    }

    public LocationCountry(String location, String locationCode) {
        super(location, locationCode);
    }

    public void setLoadingState(LoadingState state) {
        loadingState = state;
    }

    public LoadingState getLoadingState() {
        return loadingState;
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
