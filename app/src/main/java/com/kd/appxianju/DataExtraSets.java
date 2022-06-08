package com.kd.appxianju;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;
public class DataExtraSets {

    @BindingAdapter("setExtraTypeface")
    public static void setExtraTypeface(TextView textView, Typeface typeFace){
        Log.i("TAG","setExtraTypeface");
        if(textView!=null){
            textView.setTypeface(typeFace);
            textView.getPaint().setAntiAlias(false);
        }
    };
    @BindingAdapter("setExtraTextSize")
    public static void setExtraTextSize(TextView textView, int size){
        Log.i("TAG","setExtraTypeface");
        if(size>0){
           textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) size);
        }
    };

}
