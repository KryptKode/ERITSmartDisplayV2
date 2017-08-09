package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplay implements Parcelable {

    private String name;
    private String ipAddress;

    public SmartDisplay(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.ipAddress);
    }

    protected SmartDisplay(Parcel in) {
        this.name = in.readString();
        this.ipAddress = in.readString();
    }

}
