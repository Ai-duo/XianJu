package com.kd.appxianju;

import android.util.Log;

public class Dmrd {
    public String zfs,zwx,rzh,gh;

    public Dmrd(String zfs, String zwx, String rzh, String gh) {
        this.zfs = "总辐射:"+zfs+"W²/M";
        this.zwx = ""+zwx;
        this.rzh = "日照:"+rzh+"min";
        this.gh = "光合:"+gh+"W²/M";
        Log.i("TAG",toString());
    }

    @Override
    public String toString() {
        return "Dmrd{" +
                "zfs='" + zfs + '\'' +
                ", zwx='" + zwx + '\'' +
                ", rzh='" + rzh + '\'' +
                ", gh='" + gh + '\'' +
                '}';
    }
}
