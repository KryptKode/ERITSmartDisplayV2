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
    public static final String HTTP  = "http://";
    public static final String TAG = NetworkUtil.class.getSimpleName();
    public static final String WRITE_TO_DISPLAY = "writeDisplay";
    public static final String READ_DISPLAY = "readDisplay";
    public static final String BOARD_TYPE = "boardType";



    public static String buildSendingUrl(PriceBoard display){
        String url = null;

        Uri.Builder builder = Uri.parse(HTTP + display.getIpAddress()).buildUpon()
                .appendPath(WRITE_TO_DISPLAY);
        builder.appendPath(display.getMessageString());
        if (display.getPriceBoardType() != PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE) {
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


    public static String buildSyncingUrl(PriceBoard priceBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + priceBoard.getIpAddress() ).buildUpon()
                .appendPath(READ_DISPLAY);

        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String buildBoardConfigUrl(PriceBoard priceBoard){
        String url = null;
        Uri.Builder builder = Uri.parse(HTTP + priceBoard.getIpAddress() ).buildUpon()
                .appendPath(BOARD_TYPE);
        if(priceBoard.getPriceBoardType() == PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE){
            builder.appendPath(String.valueOf(priceBoard.getMessageBoardCode()));
        }else{
            builder.appendPath(String.valueOf(priceBoard.getPriceBoardCode()));
        }

        try{
            url = new URL(builder.toString()).toString();
        } catch (MalformedURLException | NullPointerException e){
            e.printStackTrace();
        }
        return url;
    }
}
