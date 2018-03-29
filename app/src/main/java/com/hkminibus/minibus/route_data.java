package com.hkminibus.minibus;


import java.util.ArrayList;
import java.util.List;

public class route_data {
    private String mRouteID;
    private String mRouteNo;
    private String mRouteName;
    //private stop_data mStopList1;
    private final List<stop_data> mStopList = new ArrayList<stop_data>();

    public route_data(String mRouteID, String mRouteNo, String mRouteName) {
        this.mRouteID = mRouteID;
        this.mRouteNo = mRouteNo;
        this.mRouteName = mRouteName;
        //this.mStopList1 = MainActivity.getmStopList(mStopList);
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


}

