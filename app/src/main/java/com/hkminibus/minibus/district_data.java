package com.hkminibus.minibus;

public class district_data{
    private String name;
    private double latitude;
    private double longitude;

    public district_data(double latitude, double longitude, String name){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public district_data(){}

    public String getName(){return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
}

