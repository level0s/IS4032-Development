package com.hkminibus.minibus;

import android.os.Parcel;
import android.os.Parcelable;

public class district_data implements Parcelable,location_data{
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

    @Override
    public int describeContents() {
        return 0;
    }

    public district_data(Parcel in) {
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
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

