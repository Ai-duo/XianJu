package com.kd.appxianju;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.kd.appxianju.databinding.ActivityFirstBinding;

public class FragmentFirst extends Fragment {
    Dmgd dmgd;
    Dmrd dmrd;
    String dm4d;
    Typeface tf;
    ActivityFirstBinding inflate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(inflate==null) {
            inflate = DataBindingUtil.inflate(inflater, R.layout.activity_first, container, false);
            inflate.setTf(tf);
        }
        if(dmgd!=null){
            inflate.setDmgd(dmgd);
        }
        if(dmrd!=null){
            inflate.setDmrd(dmrd);
        }
        if(dm4d!=null){
            inflate.setDm4d(dm4d);
        }
        return inflate.getRoot();
    }
    public void setDmgd( Dmgd dmgd){
        this.dmgd = dmgd;
        if(inflate!=null)
        inflate.setDmgd(dmgd);
    }
    public void setDmrd( Dmrd dmrd){
        this.dmrd = dmrd;
        if(inflate!=null)
            inflate.setDmrd(dmrd);
    }
    public void setDm4d( String dm4d){
        this.dm4d = dm4d;
        if(inflate!=null)
            inflate.setDm4d(dm4d);
    }
    public void setTf(Typeface tf){
        this.tf = tf;
    }
}
