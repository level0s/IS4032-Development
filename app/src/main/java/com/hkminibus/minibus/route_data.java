package com.hkminibus.minibus;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class route_data {
    private String mRouteNo;
    private String mRouteName;
    private stop_data mStopList1;

    public route_data(String mRouteNo, String mRouteName, stop_data mStopList) {
        this.mRouteNo = mRouteNo;
        this.mRouteName = mRouteName;
        this.mStopList1 = MainActivity.getmStopList(mStopList);
    }

    public route_data(){}

    public String getmRouteNo() {
        return mRouteNo;
    }
    public String getmRouteName() {return mRouteName;}


}

