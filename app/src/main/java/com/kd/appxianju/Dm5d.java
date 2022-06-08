package com.kd.appxianju;

import android.util.Log;

public class Dm5d {
    public String cm06w, cm15w, cm3w, cm45w, cm15pw, lw, mw, rw, bw;

    public Dm5d(String cm06w, String cm15w, String cm3w, String cm45w, String cm15pw, String lw, String mw, String rw, String bw) {
        this.cm06w = "0.6M:"+cm06w+"℃";
        this.cm15w = "1.5M:"+cm15w+"℃";
        this.cm3w = "3M:"+cm3w+"℃";
        this.cm45w = "4.5M:"+cm45w+"℃";
        this.cm15pw = "1.5M:"+cm15pw+"℃";
        this.lw = "左:"+lw+"℃";
        this.mw = "中:"+mw+"℃";
        this.rw = "右:"+rw+"℃";
        this.bw = "后:"+bw+"℃";
        Log.i("TAG",toString());
    }

    @Override
    public String toString() {
        return "Dm5d{" +
                "cm06w='" + cm06w + '\'' +
                ", cm15w='" + cm15w + '\'' +
                ", cm3w='" + cm3w + '\'' +
                ", cm45w='" + cm45w + '\'' +
                ", cm15pw='" + cm15pw + '\'' +
                ", lw='" + lw + '\'' +
                ", mw='" + mw + '\'' +
                ", rw='" + rw + '\'' +
                ", bw='" + bw + '\'' +
                '}';
    }
}
