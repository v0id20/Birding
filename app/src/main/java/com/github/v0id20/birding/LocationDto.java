package com.github.v0id20.birding;

public class LocationDto {

    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocationCountry mapLocationDto() {
        return new LocationCountry(2, this.getName(), this.getCode());
    }

    public LocationRegion mapLocationDto(LocationCountry country) {
        return new LocationRegion(this.getName(), this.getCode(), country);
    }

}


