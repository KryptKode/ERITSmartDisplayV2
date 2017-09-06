package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

import java.util.TreeMap;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class PriceBoard extends MessageBoard {
    public static final String TAG = PriceBoard.class.getSimpleName();
    public static final String PMS = "//P";
    public static final String DPK = "//D";
    public static final String AGO = "//A";

    private String priceString;
    private PriceBoardType priceBoardType;

    private TreeMap<String, String> priceValuesMap;


    public PriceBoard(long id, String name, String ipAddress, String messageString, int numberOfMessages, MessageBoardType messageBoardType, String priceString, PriceBoardType priceBoardType) {
        super(id, name, ipAddress, messageString, numberOfMessages, messageBoardType);
        this.priceString = priceString;
        this.priceBoardType = priceBoardType;
    }

    public PriceBoard(String name, String ipAddress, MessageBoardType messageBoardType, PriceBoardType priceBoardType) {
        super(name, ipAddress, messageBoardType);
        this.priceBoardType = priceBoardType;
    }

    public PriceBoard() {

    }

    public TreeMap<String, String> getPriceValuesMap() {
        return priceValuesMap;
    }


    public void setPriceValuesMap(TreeMap<String, String> priceValuesMap) {
        this.priceValuesMap = priceValuesMap;
    }

    public PriceBoard(Cursor cursor) throws Exception {
        super(cursor);

        this.priceString = SmartDisplayContract.getColumnString(cursor, SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING);
        Log.i(TAG, "PriceBoard: " + this.priceString);
        String boardType = SmartDisplayContract.getColumnString(cursor, SmartDisplayColumns.COLUMN_BOARD_TYPE);
        int num = Integer.parseInt(boardType.split("\\|")[1]);
        Log.i(TAG, "PriceBoard: " + num);
        this.priceBoardType = getPriceBoardTypeFromInt(num);
        if (!TextUtils.isEmpty(priceString)){
            this.priceValuesMap = DisplayBoardHelpers.parsePriceString(priceString);
        }
    }

    public static PriceBoardType getPriceBoardTypeFromInt(int code) throws Exception {
        switch (code) {
            case (1):
                return PriceBoardType.PRICE_BOARD_TYPE_ONE;
            case (2):
                return PriceBoardType.PRICE_BOARD_TYPE_TWO;
            case (3):
                return PriceBoardType.PRICE_BOARD_TYPE_THREE;
            case (4):
                return PriceBoardType.PRICE_BOARD_TYPE_FOUR;
            case (5):
                return PriceBoardType.PRICE_BOARD_TYPE_FIVE;
            case (6):
                return PriceBoardType.PRICE_BOARD_TYPE_SIX;
            case (7):
                return PriceBoardType.PRICE_BOARD_TYPE_SEVEN;
            case (8):
                return PriceBoardType.PRICE_BOARD_TYPE_EIGHT;
            case (-1):
                return PriceBoardType.PRICE_BOARD_TYPE_NONE;

            default:
                throw new Exception("Integer " + code + " is not associated with any Price board type");
        }
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



    public String createPriceSendFormat(){
        return DisplayBoardHelpers.createMessageSendFormat(this.priceValuesMap);
    }

    public String getPriceBoardCode(){
        return DisplayBoardHelpers.generatePriceBoardCode(this.priceBoardType);
    }

    public String createBoardType(){
        return this.getMessageBoardType().getNumberOfCascades() + "|" +
                this.getPriceBoardType().getNumberOfCascades();
    }


    public enum PriceBoardType {
        PRICE_BOARD_TYPE_ONE(1),
        PRICE_BOARD_TYPE_TWO(2),
        PRICE_BOARD_TYPE_THREE(3),
        PRICE_BOARD_TYPE_FOUR(4),
        PRICE_BOARD_TYPE_FIVE(5),
        PRICE_BOARD_TYPE_SIX(6),
        PRICE_BOARD_TYPE_SEVEN(7),
        PRICE_BOARD_TYPE_EIGHT(8),
        PRICE_BOARD_TYPE_NONE(-1);

        private int numberOfCascades;

        PriceBoardType(int numberOfCascades) {
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
        dest.writeSerializable(this.priceValuesMap);
    }

    protected PriceBoard(Parcel in) {
        super(in);
        this.priceString = in.readString();
        int tmpPriceBoardType = in.readInt();
        this.priceBoardType = tmpPriceBoardType == -1 ? null : PriceBoardType.values()[tmpPriceBoardType];
        this.priceValuesMap = (TreeMap<String, String>) in.readSerializable();
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
