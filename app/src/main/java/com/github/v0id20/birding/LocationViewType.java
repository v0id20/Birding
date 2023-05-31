package com.github.v0id20.birding;

public class LocationViewType {
    private int viewType;
    private String location;
    private String locationCode;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationCode() { return locationCode;}

    public void setLocationCode(String locationCode) {this.locationCode = locationCode;}

    public LocationViewType(int viewType, String location) {
        this.viewType = viewType;
        this.location = location;
    }

    public LocationViewType(int viewType, String location, String locationCode) {
        this.viewType = viewType;
        this.location = location;
        this.locationCode = locationCode;
    }

}
