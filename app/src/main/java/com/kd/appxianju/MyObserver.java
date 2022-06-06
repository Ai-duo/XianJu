package com.kd.appxianju;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class MyObserver implements LifecycleObserver {

    private static final String TAG = "MainActivity";

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.i(TAG, "Lifecycle onStart生命周期");
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Log.i(TAG, "Lifecycle onCreate生命周期");
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.i(TAG, "Lifecycle onResume生命周期");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.i(TAG, "Lifecycle onPause生命周期");
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        Log.i("MainActivity","Lifecycle onStop生命周期");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        Log.i(TAG, "Lifecycle onDestory生命周期");
    }

}