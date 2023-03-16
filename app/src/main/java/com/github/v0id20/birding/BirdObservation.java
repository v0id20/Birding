package com.github.v0id20.birding;

public class BirdObservation {
    private String comName;
    private String sciName;
    private String obsDt;
    private String countryName;
    private String subnational1Name;

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    private  String locName;

    public BirdObservation(String comName, String sciName, String date, String country, String location){
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
}
