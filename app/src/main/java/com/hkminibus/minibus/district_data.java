package com.hkminibus.minibus;

public class district_data{
    private String mDistrict;
    private double mLat;
    private double mLng;

    public district_data(String mDistrict, double mLat, double mLng){
        this.mDistrict = "地區: "+mDistrict;
        this.mLat = mLat;
        this.mLng = mLng;
    }

    public String getmDistrict(){return mDistrict;}
    public double getmLat() {return mLat;}
    public double getmLng() {return mLng;}
}
