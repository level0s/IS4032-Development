package com.hkminibus.minibus;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class stop_data implements Parcelable, location_data{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public stop_data(Parcel in){
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Parcelable.Creator<stop_data> CREATOR = new Parcelable.Creator<stop_data>()
    {
        public stop_data createFromParcel(Parcel in)
        {
            return new stop_data(in);
        }
        public stop_data[] newArray(int size)
        {
            return new stop_data[size];
        }
    };

}
    git stash