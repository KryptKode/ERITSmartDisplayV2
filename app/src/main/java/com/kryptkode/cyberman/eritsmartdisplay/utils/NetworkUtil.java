package com.kryptkode.cyberman.eritsmartdisplay.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cyberman on 7/1/2017.
 */

public abstract class NetworkUtil extends Context {

    public static final String TAG = NetworkUtil.class.getSimpleName();
    public static final String BASE_IP = "http://192.168.43.";
    public static final String WRITE_TO_DISPLAY = "writeDisplay";
    public static final String READ_DISPLAY = "readDisplay";



    public static String buildSendingUrl(PriceBoard display){
        String url = null;

        Uri.Builder builder = Uri.parse(BASE_IP).buildUpon()
                .appendPath(WRITE_TO_DISPLAY);
        builder.appendPath(display.getMessageString());
        if (!TextUtils.isEmpty(display.getPriceString())) {
            builder.appendPath(display.getIpAddress());
        }

        try{
            url = new URL(builder.toString()).toString();
            Log.e(TAG, "buildSendingUrl: " + url);
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }


    public static String buildSyncingUrl(){
        String url = null;
        Uri.Builder builder = Uri.parse(BASE_IP).buildUpon()
                .appendPath(READ_DISPLAY);

        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }
}
