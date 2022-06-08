package com.kd.appxianju;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kd.appxianju.databinding.ActivityFirstBinding;
import com.kd.appxianju.databinding.ActivitySecondBinding;

public class FragmentSecond extends Fragment {
    Dm5d dm5d;
    Typeface tf;
    ActivitySecondBinding inflate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(inflate==null) {
            inflate = DataBindingUtil.inflate(inflater, R.layout.activity_second, container, false);
            inflate.setTf(tf);
        }
        if(dm5d!=null){
            inflate.setDm5d(dm5d);
        }

        return inflate.getRoot();
    }
    public void setDm5d( Dm5d dmgd){
        this.dm5d = dmgd;
        if(inflate!=null)
            inflate.setDm5d(dmgd);
    }
    public void setTf(Typeface tf){
        this.tf = tf;
    }
}
