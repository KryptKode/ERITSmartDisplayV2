package com.kryptkode.cyberman.eritsmartdisplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.kryptkode.cyberman.eritsmartdisplay.utils.WifiHotspot;

public class SplashScreen extends AppCompatActivity {
    private static  final int SPLASH_DISPLAY_LENGTH = 1500;

    // Declare the Handler as a member variable
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        delay();

    }

    private void delay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(this, EritSmartDisplayActivity.class);
        startActivity(mainIntent);
        finish();
    }









}

