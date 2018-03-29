package com.hkminibus.minibus;

public class landmark_data{

    private double latitude;
    private double longitude;
    private String name;

    public landmark_data(String name){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public landmark_data(){}

    public String getName(){return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
}
