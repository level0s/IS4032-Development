package com.hkminibus.minibus;

import android.os.Parcel;
import android.os.Parcelable;

public class district_data implements Parcelable,location_data{
    private String name;
    private double latitude;
    private double longitude;
    private double radius;

    public district_data(double latitude, double longitude, String name, double radius){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public district_data(){}

    public String getName(){return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public double getRadius() {return radius;}

    @Override
    public int describeContents() {
        return 0;
    }

    public district_data(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        radius = in.readDouble();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(radius);
    }

    public static final Parcelable.Creator<district_data> CREATOR = new Parcelable.Creator<district_data>()
    {
        public district_data createFromParcel(Parcel in)
        {
            return new district_data(in);
        }
        public district_data[] newArray(int size)
        {
            return new district_data[size];
        }
    };
}

