package com.hkminibus.minibus;

import java.util.ArrayList;
import java.util.List;

public class stop_data{
    private String name;
    private double latitude;
    private double longitude;

    public stop_data(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public stop_data(){}
    public String getName(){return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}

}
