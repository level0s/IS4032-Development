package com.hkminibus.minibus;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class route_data implements Parcelable{
    private String mRouteID;
    private String mRouteNo;
    private String mRouteName;
    private List<stop_data> mStopList = new ArrayList<stop_data>();

    public route_data(String mRouteID, String mRouteNo, String mRouteName) {
        this.mRouteID = mRouteID;
        this.mRouteNo = mRouteNo;
        this.mRouteName = mRouteName;
    }

    public void setmStopList(stop_data s){
        mStopList.add(s);
    }

    public route_data(){}

    public String getmRouteID(){return mRouteID;}
    public String getmRouteNo() {
        return mRouteNo;
    }
    public String getmRouteName() {return mRouteName;}
    public List<stop_data>getmStopList(){return mStopList;}


    @Override
    public int describeContents() {
        return 0;
    }

    public route_data(Parcel in) {
        mRouteID = in.readString();
        mRouteNo = in.readString();
        mRouteName = in.readString();
        //List<stop_data>mStopList = new ArrayList<stop_data>();
        //in.readList(mStopList,stop_data.class.getClassLoader());
        mStopList = in.readArrayList(stop_data.class.getClassLoader());
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mRouteID);
        parcel.writeString(mRouteNo);
        parcel.writeString(mRouteName);
        parcel.writeList(mStopList);
    }

    public static final Parcelable.Creator<route_data> CREATOR = new Parcelable.Creator<route_data>()
    {
        public route_data createFromParcel(Parcel in)
        {
            return new route_data(in);
        }
        public route_data[] newArray(int size)
        {
            return new route_data[size];
        }
    };
}

