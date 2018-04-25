package com.hkminibus.minibus;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class driving_mini_data implements Parcelable {
    private String carSize;
    private boolean driving;
    private boolean full;
    private double lat;
    private double lng;
    private String mPlateNo;
    private String mRouteName;
    private String mRouteNo;
    private boolean nextStop;
    private String stopName;
    private String type;

    public driving_mini_data() {}

    public driving_mini_data(String carSize, boolean driving, boolean full, double lat, double lng, String mPlateNo,
                      String mRouteName, String mRouteNo, boolean nextStop, String stopName,String type){
        this.carSize = carSize;
        this.driving = driving;
        this.full = full;
        this.lat = lat;
        this.lng = lng;
        this.mPlateNo = mPlateNo;
        this.mRouteName = mRouteName;
        this.mRouteNo = mRouteNo;
        this.nextStop = nextStop;
        this.stopName = stopName;
        this.type = type;
    }

    public String getCarSize() {return carSize;}
    public boolean isDriving() {return driving;}
    public boolean isFull() {return full;}
    public double getLat() {return lat;}
    public double getLng() {return lng;}
    public String getmPlateNo() {return mPlateNo;}
    public String getmRouteName() {return mRouteName;}
    public String getmRouteNo() {return mRouteNo;}
    public boolean isNextStop() {return nextStop;}
    public String getStopName() {return stopName;}
    public String getType() {return type;}

    @Override
    public int describeContents() {
        return 0;
    }

    public driving_mini_data(Parcel in){
        carSize = in.readString();
        driving = in.readInt() == 1;
        full = in.readInt() == 1;
        lat = in.readDouble();
        lng = in.readDouble();
        mPlateNo = in.readString();
        mRouteName = in.readString();
        mRouteNo = in.readString();
        nextStop = in.readInt() == 1;
        stopName = in.readString();
        type = in.readString();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(carSize);
        parcel.writeInt(driving? 1:0);
        parcel.writeInt(full? 1:0);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(mPlateNo);
        parcel.writeString(mRouteName);
        parcel.writeString(mRouteNo);
        parcel.writeInt(nextStop? 1:0);
        parcel.writeString(stopName);
        parcel.writeString(type);
    }

    public static final Parcelable.Creator<driving_mini_data> CREATOR = new Parcelable.Creator<driving_mini_data>()
    {
        public driving_mini_data createFromParcel(Parcel in)
        {
            return new driving_mini_data(in);
        }
        public driving_mini_data[] newArray(int size)
        {
            return new driving_mini_data[size];
        }
    };

}
