package com.kd.appxianju;

import android.util.Log;

public class Dmsd {
    public String sf5,sf10,sf20,sf30,sf40;

    public Dmsd(String sf5, String sf10, String sf20, String sf30, String sf40) {
        this.sf5 = "10CM:"+sf5+"%";
        this.sf10 = "20CM:"+sf10+"%";
        this.sf20 = "30CM:"+sf20+"%";
        this.sf30 = "40CM:"+sf30+"%";
        this.sf40 = "50CM:"+sf40+"%";
        Log.i("TAG",toString());
    }

    @Override
    public String toString() {
        return "Dmsd{" +
                "sf5='" + sf5 + '\'' +
                ", sf10='" + sf10 + '\'' +
                ", sf20='" + sf20 + '\'' +
                ", sf30='" + sf30 + '\'' +
                ", sf40='" + sf40 + '\'' +
                '}';
    }
}
