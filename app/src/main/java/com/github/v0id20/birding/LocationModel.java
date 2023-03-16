package com.github.v0id20.birding;

import java.util.ArrayList;

public class LocationModel {
    public ArrayList<LocationViewType> getLocationList() {
        return locationList;
    }

    private ArrayList<LocationViewType> locationList;

    public LocationModel(){
        locationList = new ArrayList<LocationViewType>();
        locationList.add(new LocationViewType(1, "My location"));
        locationList.add(new LocationViewType(2, "Albania", "AL"));
        locationList.add(new LocationViewType(2, "Botswana", "BW"));
        locationList.add(new LocationViewType(2, "Brazil", "BR"));
        locationList.add(new LocationViewType(2, "Canada", "CA"));
        locationList.add(new LocationViewType(2, "Ethiopia", "ET"));
        locationList.add(new LocationViewType(2, "Germany", "DE"));
        locationList.add(new LocationViewType(2, "Guatemala", "GT"));
        locationList.add(new LocationViewType(2, "Kazakhstan", "KZ"));
        locationList.add(new LocationViewType(2, "Latvia", "LV"));
        locationList.add(new LocationViewType(2, "Malawi", "MW"));
        locationList.add(new LocationViewType(2, "Romania", "RO"));
        locationList.add(new LocationViewType(2, "Slovenia", "SI"));
        locationList.add(new LocationViewType(2, "Turkey", "TR"));
        locationList.add(new LocationViewType(2, "USA", "US"));
        locationList.add(new LocationViewType(2, "Zambia", "ZM"));
        locationList.add(new LocationViewType(2, "Zambia", "ZM"));
    }
}
