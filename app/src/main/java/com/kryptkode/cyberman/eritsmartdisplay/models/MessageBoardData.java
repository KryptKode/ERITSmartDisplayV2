package com.kryptkode.cyberman.eritsmartdisplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class MessageBoardData implements Parcelable {

    ArrayList<String> messagesList = new ArrayList<>();

    public ArrayList<String> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(ArrayList<String> messagesList) {
        this.messagesList = messagesList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.messagesList);
    }

    public MessageBoardData() {
    }

    protected MessageBoardData(Parcel in) {
        this.messagesList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<MessageBoardData> CREATOR = new Parcelable.Creator<MessageBoardData>() {
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
