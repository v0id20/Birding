package com.github.v0id20.birding;

public class LocationViewType {
    private int viewType;
    private String name;
    private String code;


    public int getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

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

    public LocationViewType(int viewType, String location) {
        this.viewType = viewType;
        this.name = location;
    }

}
