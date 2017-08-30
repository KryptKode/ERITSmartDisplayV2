package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplay implements Parcelable {
    public static final String TAG = SmartDisplay.class.getSimpleName();
    private long id;
    private String name;
    private String ipAddress;

    public SmartDisplay(long id, String name, String ipAddress) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public SmartDisplay( String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public SmartDisplay(){
        //empty constructor
    }
    public Uri buildBoardUri(){
        return SmartDisplayColumns.buildDisplayUri(this.getId());
    }

    public SmartDisplay(Cursor cursor){
        this.id = SmartDisplayContract.getColumnLong(cursor, SmartDisplayColumns._ID);
        Log.i(TAG, "SmartDisplay: " + this.id);
        this.name = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayColumns.COLUMN_NAME);

        Log.i(TAG, "SmartDisplay: " + this.name);
        this.ipAddress = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayColumns.COLUMN_IP_ADDRESS);
        Log.i(TAG, "SmartDisplay: " + this.ipAddress);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ipAddress);
    }

    protected SmartDisplay(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.ipAddress = in.readString();
    }

}
