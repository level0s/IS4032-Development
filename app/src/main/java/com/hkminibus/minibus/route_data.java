package com.hkminibus.minibus;

public class route_data {
    private String mRouteNo;
    private String mRouteName;
    /*private String 名稱;
    private String 號碼;
    private String 小巴種類;
    private String 車牌;*/

    public route_data(){}

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
