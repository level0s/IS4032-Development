package com.hkminibus.minibus;

import java.util.ArrayList;
import java.util.List;

public class stop_data{
    private String mStop;
    private double mLat;
    private double mLng;

    public stop_data(String mStop, double mLat, double mLng){
        this.mStop = mStop;
        this.mLat = mLat;
        this.mLng = mLng;
    }

    public stop_data(){}

    public String getmStop(){return mStop;}
    public double getmLat() {return mLat;}
    public double getmLng() {return mLng;}

}
