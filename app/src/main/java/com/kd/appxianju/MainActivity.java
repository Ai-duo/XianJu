package com.kd.appxianju;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kd.appxianju.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding main;
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.time);
        initFragments();
        //getLifecycle().addObserver(new MyObserver());
        receiverLiveData();
        initTimer();
        changeFragment();

    }

    FragmentFirst fragmentFirst;
    FragmentSecond fragmentSecond;
    FragmentThird fragmentThird;

    private void initFragments() {
        fragmentFirst = new FragmentFirst();
        fragmentSecond = new FragmentSecond();
        fragmentThird = new FragmentThird();
    }

    private void changeFragment() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 20 * 1000);
    }

    Timer timer;
    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm", Locale.CHINA);
   int index = 0;
    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String date = format.format(new Date());
                LiveDataBus.get().with("time").postValue(date);
                Log.i("TAG", "发送。。。。。。");
                int i = index%3;
                if(i==0){
                    LiveDataBus.get().with("fragment").postValue(fragmentFirst);
                }else if(i==1){
                    LiveDataBus.get().with("fragment").postValue(fragmentSecond);
                }else{
                    LiveDataBus.get().with("fragment").postValue(fragmentThird);
                }
                index++;
                if(index==3000){
                    index = 0;
                }
            }
        }, 0, 15000);
    }

    public void receiverLiveData() {
        LiveDataBus.get().with("time", String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String time) {
                Log.i("TAG", "收到数据》》》》");
                //  main.setTime(time);
                timeView.setText(time);
            }
        });
        LiveDataBus.get().with("fragment", Fragment.class).observe(this, new Observer<Fragment>() {
            @Override
            public void onChanged(Fragment fragment) {
                Log.i("TAG", "收到数据》》》》");
                //  main.setTime(time);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content, fragment).commit();
            }
        });
    }

   /* @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }*/
}