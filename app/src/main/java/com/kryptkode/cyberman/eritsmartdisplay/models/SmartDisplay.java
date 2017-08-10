package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplay implements Parcelable {
    private long id;
    private String name;
    private String ipAddress;

    public SmartDisplay(long id, String name, String ipAddress) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
    }


    public SmartDisplay(Cursor cursor){
        this.id = SmartDisplayContract.getColumnLong(cursor, SmartDisplayColumns._ID);
        this.name = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayColumns.COLUMN_NAME);
        this.ipAddress = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayColumns.COLUMN_IP_ADDRESS);
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

    public static final Creator<SmartDisplay> CREATOR = new Creator<SmartDisplay>() {
        @Override
        public SmartDisplay createFromParcel(Parcel source) {
            return new SmartDisplay(source);
        }

        @Override
        public SmartDisplay[] newArray(int size) {
            return new SmartDisplay[size];
        }
    };
}
