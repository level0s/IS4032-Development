package com.hkminibus.minibus;

public class landmark_data{
    private String mLandmark;
    private double mLat;
    private double mLng;

    public landmark_data(String mLandmark, double mLat, double mLng){
        this.mLandmark = mLandmark;
        this.mLat = mLat;
        this.mLng = mLng;
    }

    public String getmLandmark(){return mLandmark;}
    public double getmLat() {return mLat;}
    public double getmLng() {return mLng;}
}
