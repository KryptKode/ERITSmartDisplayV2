package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.os.Parcel;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class MessageBoard extends SmartDisplay {
    private String messageString;
    private MessageBoardType messageBoardType;

    public MessageBoard(String name, String ipAddress) {
        super(name, ipAddress);
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public MessageBoardType getMessageBoardType() {
        return messageBoardType;
    }

    public void setMessageBoardType(MessageBoardType messageBoardType) {
        this.messageBoardType = messageBoardType;
    }

    public enum MessageBoardType{
        MESSAGE_BOARD_TYPE_TWO(2),
        MESSAGE_BOARD_TYPE_THREE(3),
        MESSAGE_BOARD_TYPE_FOUR(4),
        MESSAGE_BOARD_TYPE_FIVE(5),
        MESSAGE_BOARD_TYPE_SIX(6),
        MESSAGE_BOARD_TYPE_SEVEN(7),
        MESSAGE_BOARD_TYPE_EIGHT(8),
        MESSAGE_BOARD_TYPE_NINE(9),
        MESSAGE_BOARD_TYPE_TEN(10);

        private int numberOfCascades;
        MessageBoardType(int numberOfCascades) {
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
        dest.writeString(this.messageString);
        dest.writeInt(this.messageBoardType == null ? -1 : this.messageBoardType.ordinal());
    }

    protected MessageBoard(Parcel in) {
        super(in);
        this.messageString = in.readString();
        int tmpMessageBoardType = in.readInt();
        this.messageBoardType = tmpMessageBoardType == -1 ? null : MessageBoardType.values()[tmpMessageBoardType];
    }

}
