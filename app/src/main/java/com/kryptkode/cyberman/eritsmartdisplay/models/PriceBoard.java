package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.os.Parcel;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class PriceBoard extends MessageBoard {
    private String priceString;
    private PriceBoardType priceBoardType;

    public PriceBoard(String name, String ipAddress) {
        super(name, ipAddress);
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public PriceBoardType getPriceBoardType() {
        return priceBoardType;
    }

    public void setPriceBoardType(PriceBoardType priceBoardType) {
        this.priceBoardType = priceBoardType;
    }

    public enum PriceBoardType{
        PRICE_BOARD_TYPE_ONE(1),
        PRICE_BOARD_TYPE_TWO(2);

        private int numberOfCascades;
         PriceBoardType (int numberOfCascades){
            this.numberOfCascades = numberOfCascades;
        }

        public int getNumberOfCascades() {
            return numberOfCascades;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.priceString);
        dest.writeInt(this.priceBoardType == null ? -1 : this.priceBoardType.ordinal());
    }

    protected PriceBoard(Parcel in) {
        super(in);
        this.priceString = in.readString();
        int tmpPriceBoardType = in.readInt();
        this.priceBoardType = tmpPriceBoardType == -1 ? null : PriceBoardType.values()[tmpPriceBoardType];
    }

    public static final Creator<PriceBoard> CREATOR = new Creator<PriceBoard>() {
        @Override
        public PriceBoard createFromParcel(Parcel source) {
            return new PriceBoard(source);
        }

        @Override
        public PriceBoard[] newArray(int size) {
            return new PriceBoard[size];
        }
    };
}
