package com.github.v0id20.birding.birdobservationitem;

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
    public static final String SUBNATIONAL_NAME_EXTRA = "Subnational name";
    private final String commonName;
    private final String scientificName;
    private final String countryName;
    private String locationName;
    private final String subnational1Name;
    private final String time;
    private final String latitude;
    private final String longitude;
    private final int howMany;
    private boolean locationDecoded;


    public BirdObservation(String commonName, String sciName, String countryName, String subnational1Name, String locationName, String date, String time, String latitude, String longitude, int howMany) {
        super(date);
        this.commonName = commonName;
        this.scientificName = sciName;
        this.countryName = countryName;
        this.subnational1Name = subnational1Name;
        this.locationName = locationName;
        this.time = time;
        this.howMany = howMany;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
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

    public String getScientificName() {
        return scientificName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getSubnational1Name() {
        return subnational1Name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getHowMany() {
        return howMany;
    }

    public boolean isLocationDecoded() {
        return locationDecoded;
    }

    public void setLocationDecoded(boolean locationDecoded) {
        this.locationDecoded = locationDecoded;
    }
}
