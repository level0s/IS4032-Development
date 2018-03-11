package com.hkminibus.minibus;

public class route_data {
    private String mRouteNo;
    private String mRouteName;

    public route_data(String mRouteNo, String mRouteName) {
        this.mRouteNo = mRouteNo;
        this.mRouteName = mRouteName;
    }

    public String getmRouteNo() {
        return mRouteNo;
    }

    public String getmRouteName() {
        return mRouteName;
    }

}
