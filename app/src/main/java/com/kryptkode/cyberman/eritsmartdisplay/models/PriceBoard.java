package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.database.Cursor;
import android.os.Parcel;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class PriceBoard extends MessageBoard {
    public static final String TAG = PriceBoard.class.getSimpleName();
    private String priceString;
    private PriceBoardType priceBoardType;


    public PriceBoard(long id, String name, String ipAddress, String messageString, int numberOfMessages, MessageBoardType messageBoardType, String priceString, PriceBoardType priceBoardType) {
        super(id, name, ipAddress, messageString, numberOfMessages, messageBoardType);
        this.priceString = priceString;
        this.priceBoardType = priceBoardType;
    }

    public PriceBoard( String name, String ipAddress,  MessageBoardType messageBoardType,  PriceBoardType priceBoardType) {
        super( name, ipAddress, messageBoardType);
        this.priceBoardType = priceBoardType;
    }

    public PriceBoard(){

    }

    public PriceBoard(Cursor cursor) throws Exception {
        super(cursor);

        this.priceString = SmartDisplayContract.getColumnString(cursor, SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING);
        Log.i(TAG, "PriceBoard: " + this.priceString);
        String boardType = SmartDisplayContract.getColumnString(cursor, SmartDisplayColumns.COLUMN_BOARD_TYPE);
        int num = Integer.parseInt(boardType.split("\\|")[1]);
        Log.i(TAG, "PriceBoard: " + num);
        this.priceBoardType = getPriceBoardTypeFromInt(num);
    }

    public static PriceBoardType getPriceBoardTypeFromInt(int code) throws Exception {
        switch (code){
            case (1):
                return PriceBoardType.PRICE_BOARD_TYPE_ONE;
            case (2):
                return PriceBoardType.PRICE_BOARD_TYPE_TWO;
            case 3:
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

    public long getPriceId() {
        return super.getMessageId();
    }

    public String getPriceName() {
        return super.getMessageName();
    }

    public String getPriceIpAddress() {
        return super.getMessageIpAddress();
    }


    public MessageBoardType getPriceMessageBoardType() {
        return super.getMessageBoardType();
    }

   /* public int getNumberOfMessages(){
       return super.getNumberOfMessages();
    }*/

    public String getPriceMessageString() {
        return super.getMessageString();
    }

    public enum PriceBoardType{
        PRICE_BOARD_TYPE_ONE(1),
        PRICE_BOARD_TYPE_TWO(2),
        PRICE_BOARD_TYPE_NONE(3);

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
