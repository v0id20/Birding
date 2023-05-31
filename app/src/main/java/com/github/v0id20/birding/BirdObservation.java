package com.github.v0id20.birding;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BirdObservation {
    public static final String COMMON_NAME_EXTRA = "Common name";
    public static final String SCIENTIFIC_NAME_EXTRA = "Scientific name";
    public static final String LOCATION_NAME_EXTRA = "Location name";
    public static final String COUNTRY_NAME_EXTRA = "Country name";
    public static final String REGION_CODE_EXTRA = "Region Code";
    public static final String LATITUDE_EXTRA = "Latitude";
    public static final String LONGITUDE_EXTRA = "Longitude";
    public static final String OBSERVATION_DATE_EXTRA = "Observation date";
    private String comName;
    private String sciName;
    private String obsDt;

    private String date;
    // private String obsTime;
    private String countryName;
    private String subnational1Name;
    private String time;
    private String locName;
    private String latitude;
    private String longitude;

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public BirdObservation(String comName, String sciName, String date, String country, String location) {
        this.comName = comName;
        this.sciName = sciName;
        obsDt = date;
        this.countryName = country;
        this.subnational1Name = location;
    }


    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public String getObsDt() {
        return obsDt;
    }

    public void setObsDt(String obsDt) {
        this.obsDt = obsDt;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getSubnational1Name() {
        return subnational1Name;
    }

    public void setSubnational1Name(String subnational1Name) {
        this.subnational1Name = subnational1Name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
