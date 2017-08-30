package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.TreeMap;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class PriceBoardData implements Parcelable {
    public static final String PMS = "//P";
    public static final String DPK = "//D";
    public static final String AGO = "//A";

    private String pmsPrice;
    private String dpkPrice;
    private String agoPrice;



    public PriceBoardData(String pmsPrice, String dpkPrice, String agoPrice) {
        this.pmsPrice = pmsPrice;
        this.dpkPrice = dpkPrice;
        this.agoPrice = agoPrice;
    }

    public String getPmsPrice() {
        return pmsPrice;
    }

    public void setPmsPrice(String pmsPrice) {
        this.pmsPrice = pmsPrice;
    }

    public String getDpkPrice() {
        return dpkPrice;
    }

    public void setDpkPrice(String dpkPrice) {
        this.dpkPrice = dpkPrice;
    }

    public String getAgoPrice() {
        return agoPrice;
    }

    public void setAgoPrice(String agoPrice) {
        this.agoPrice = agoPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pmsPrice);
        dest.writeString(this.dpkPrice);
        dest.writeString(this.agoPrice);
    }

    public PriceBoardData() {
    }

    protected PriceBoardData(Parcel in) {
        this.pmsPrice = in.readString();
        this.dpkPrice = in.readString();
        this.agoPrice = in.readString();
    }

    public static final Parcelable.Creator<PriceBoardData> CREATOR = new Parcelable.Creator<PriceBoardData>() {
        @Override
        public PriceBoardData createFromParcel(Parcel source) {
            return new PriceBoardData(source);
        }

        @Override
        public PriceBoardData[] newArray(int size) {
            return new PriceBoardData[size];
        }
    };
}
