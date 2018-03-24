package com.hkminibus.minibus;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class route_data {
    private String mRouteNo;
    private String mRouteName;
    //private ArrayList<stop_data> mStopList = new ArrayList<>();
    /*private String 名稱;
    private String 號碼;
    private String 小巴種類;
    private String 車牌;*/

    public route_data(){}

    public route_data(String mRouteNo, String mRouteName) {
        this.mRouteNo = mRouteNo;
        this.mRouteName = mRouteName;
        //.mStopList = mStopList;
    }

    public String getmRouteNo() {
        return mRouteNo;
    }
    public String getmRouteName() {
        return mRouteName;
    }
    /*public ArrayList<stop_data> getmStopList() {
        return mStopList;
    }*/

}

