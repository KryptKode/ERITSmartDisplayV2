package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.TreeMap;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class MessageBoardData implements Parcelable {
    public static final String MSG = "//M";
    private TreeMap<String, String > messagesMap = new TreeMap<>();

    public TreeMap<String, String > getMessagesList() {
        return messagesMap;
    }

    public void setMessagesList(TreeMap<String, String > messagesMap) {
        this.messagesMap = messagesMap;
    }

    public MessageBoardData(TreeMap<String, String> messagesMap) {
        this.messagesMap = messagesMap;
    }

    public MessageBoardData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.messagesMap);
    }

    protected MessageBoardData(Parcel in) {
        this.messagesMap = (TreeMap<String, String>) in.readSerializable();
    }

    public static final Creator<MessageBoardData> CREATOR = new Creator<MessageBoardData>() {
        @Override
        public MessageBoardData createFromParcel(Parcel source) {
            return new MessageBoardData(source);
        }

        @Override
        public MessageBoardData[] newArray(int size) {
            return new MessageBoardData[size];
        }
    };
}
