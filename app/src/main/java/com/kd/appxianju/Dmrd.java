package com.kd.appxianju;

import android.util.Log;

public class Dmrd {
    public String zfs,rzh,gh;

    public Dmrd(String zfs, String rzh, String gh) {
        this.zfs = "总辐射:"+zfs+"W²/M";
        this.rzh = "小时日照:"+rzh+"min";
        this.gh = "光合:"+gh+"W²/M";
        Log.i("TAG",toString());
    }

    @Override
    public String toString() {
        return "Dmrd{" +
                "zfs='" + zfs + '\'' +
                ", rzh='" + rzh + '\'' +
                ", gh='" + gh + '\'' +
                '}';
    }
}
