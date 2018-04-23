package com.hkminibus.minibus;

import android.os.Parcel;
import android.os.Parcelable;

public class onclick_data implements Parcelable{
    private String mRouteID;
    private int position;
    private int clicked;


    public onclick_data(String mRouteID, int position, int clicked){
            this.mRouteID = mRouteID;
            this.position = position;
            this.clicked = clicked;
    }


    public onclick_data(){}

    public String getmRouteID(){return mRouteID;}
    public int getPosition() {return position;}
    public int getClicked() {return  clicked;}

    public void setmRouteID(String n){
        mRouteID = n;
    }
    public void setPosition(int n){
        position = n;
    }

    public void setClicked(int n){
        clicked = n;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public onclick_data(Parcel in) {
        mRouteID = in.readString();
        position = in.readInt();
        clicked = in.readInt();

    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mRouteID);
        parcel.writeInt(position);
        parcel.writeInt(clicked);

    }

    public static final Creator<onclick_data> CREATOR = new Creator<onclick_data>()
    {
        public onclick_data createFromParcel(Parcel in)
        {
            return new onclick_data(in);
        }
        public onclick_data[] newArray(int size)
        {
            return new onclick_data[size];
        }
    };
}

