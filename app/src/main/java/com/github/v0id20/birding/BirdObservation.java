package com.github.v0id20.birding;

public class BirdObservation extends BirdObservationItem {
    public static final String COMMON_NAME_EXTRA = "Common name";
    public static final String SCIENTIFIC_NAME_EXTRA = "Scientific name";
    public static final String LOCATION_NAME_EXTRA = "Location name";
    public static final String COUNTRY_NAME_EXTRA = "Country name";
    public static final String REGION_NAME_EXTRA = "Region name";
    public static final String REGION_CODE_EXTRA = "Region Code";
    public static final String LATITUDE_EXTRA = "Latitude";
    public static final String LONGITUDE_EXTRA = "Longitude";
    public static final String OBSERVATION_DATE_EXTRA = "Observation date";
    public static final String OBSERVATION_TIME_EXTRA = "Observation time";
    public static final String HOW_MANY_EXTRA = "How many";
    private String commonName;
    private String scientificName;
    private String countryName;
    private String locationName;
    private String subnational1Name;
    private String time;
    private String latitude;
    private String longitude;
    private int howMany;

    public BirdObservation() {
    }


    public String getDate() {
        return observationDate;
    }

    public void setDate(String date) {
        observationDate = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
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


    public int getHowMany() {
        return howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }
}
