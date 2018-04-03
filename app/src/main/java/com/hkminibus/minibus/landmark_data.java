package com.hkminibus.minibus;

import android.os.Parcel;
import android.os.Parcelable;

public class landmark_data implements Parcelable,location_data{

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

    @Override
    public int describeContents() {
        return 0;
    }

    public landmark_data(Parcel in){
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

    public static final Parcelable.Creator<landmark_data> CREATOR = new Parcelable.Creator<landmark_data>()
    {
        public landmark_data createFromParcel(Parcel in)
        {
            return new landmark_data(in);
        }
        public landmark_data[] newArray(int size)
        {
            return new landmark_data[size];
        }
    };
}
