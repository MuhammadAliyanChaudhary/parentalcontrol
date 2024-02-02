package com.example.parentalcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.example.parentalcontrol.MainActivity;
import com.example.parentalcontrol.R;

public class SplashActivity extends BaseActivity {

    private final int DELAY_LONG = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashDelay();
    }



    private void splashDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(MainActivity.class, null);
                finish();
            }
        },DELAY_LONG);
    }



}