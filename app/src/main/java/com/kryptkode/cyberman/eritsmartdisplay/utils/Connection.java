package com.kryptkode.cyberman.eritsmartdisplay.utils;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Cyberman on 7/1/2017.
 */

public class Connection {


    private static final String TAG = Connection.class.getSimpleName();

    public static String getData(String url){
        Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            return response.body().toString();
        } catch (Exception e) {
            Log.e(TAG, "getData: " + e.getLocalizedMessage());
        }
        return null; //if there is an error, return null
    }
    public static boolean sendData(String url){
        String acknowledgement;
        Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            acknowledgement = response.body().toString();
            return !acknowledgement.isEmpty();
        } catch (Exception e) {
            Log.e(TAG, "getData: " + e.getLocalizedMessage());
        }
        return false; //if there is an error, return null
    }


}
