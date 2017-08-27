package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.database.Cursor;
import android.os.Parcel;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class MessageBoard extends SmartDisplay {
    public static final String TAG = MessageBoard.class.getSimpleName();
    private String messageString;
    private int numberOfMessages;
    private MessageBoardType messageBoardType;

    public MessageBoard(long id, String name, String ipAddress, String messageString, int numberOfMessages, MessageBoardType messageBoardType) {
        super(id, name, ipAddress);
        this.messageString = messageString;
        this.messageBoardType = messageBoardType;
        this.numberOfMessages = numberOfMessages;
    }

    public MessageBoard( String name, String ipAddress, MessageBoardType messageBoardType) {
        super( name, ipAddress);
        this.messageBoardType = messageBoardType;
    }

    public MessageBoard(){

    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public MessageBoard(Cursor cursor) throws Exception {
        super(cursor);
        this.messageString = SmartDisplayContract.getColumnString(cursor, SmartDisplayColumns.COLUMN_MESSAGE_STRING);
        Log.i(TAG, "MessageBoard: " + this.messageString);
        String boardType = SmartDisplayContract.getColumnString(cursor, SmartDisplayColumns.COLUMN_BOARD_TYPE);
        int num = Integer.parseInt(boardType.split("\\|")[0]);
        Log.i(TAG, "MessageBoard: " + num);
        this.messageBoardType = getMessageBoardTypeFromInt(num);
        this.numberOfMessages = SmartDisplayContract.getColumnInt(cursor, SmartDisplayColumns.COLUMN_NUMBER_OF_MSG);

    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }
    public static MessageBoardType getMessageBoardTypeFromInt(int code) throws Exception {
        switch (code){
            case (2):
                return MessageBoardType.MESSAGE_BOARD_TYPE_TWO;
            case (3):
                return MessageBoardType.MESSAGE_BOARD_TYPE_THREE;
            case (4):
                return MessageBoardType.MESSAGE_BOARD_TYPE_FOUR;
            case (5):
                return MessageBoardType.MESSAGE_BOARD_TYPE_FIVE;

            case (6):
                return MessageBoardType.MESSAGE_BOARD_TYPE_SIX;
            case (7):
                return MessageBoardType.MESSAGE_BOARD_TYPE_SEVEN;
            case (8):
                return MessageBoardType.MESSAGE_BOARD_TYPE_EIGHT;
            case (9):
                return MessageBoardType.MESSAGE_BOARD_TYPE_NINE;
            case (10):
                return MessageBoardType.MESSAGE_BOARD_TYPE_TEN;
            default:
                throw new Exception("Integer " + code + " is not associated with any Message board type");
        }
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


    public long getMessageId() {
        return super.getId();
    }

    public String getMessageIpAddress() {
        return super.getIpAddress();
    }

    public String getMessageName() {
        return super.getName();
    }

}
