package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoardData;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoardData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;

import static com.kryptkode.cyberman.eritsmartdisplay.DetailFragment.TAG;
import static com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoardData.MSG;
import static com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoardData.AGO;
import static com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoardData.DPK;
import static com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoardData.PMS;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class DetailFragmentHelper {

    public static final String MESSAGE = "message";
    public static final String MESSAGES = "messages";


    private static MessageBoardData parseMessageString(String data, int numOfMessagees) {
        TreeMap<String , String> messagesMap  = new TreeMap<>();
        int index1, index2;
        for (int i = 1; i <= numOfMessagees; i++) {
            if (i == numOfMessagees){
                index1 = data.indexOf(MSG + i); // //M1
                if (index1 != -1){
                    messagesMap.put(MSG + i, data.substring(index1 + 4, data.length()));
                    Log.i(TAG, "parseMessageString: " + index1);
                    Log.i(TAG, "parseMessageString: " + messagesMap.get(MSG + i));
                }
            }else{

                index1 = data.indexOf(MSG + i); // //M1
                index2 = data.indexOf(MSG + (i + 1)); // //M2

                if (index1 != -1 && index2 != -1) {
                   messagesMap.put(MSG + i, data.substring(index1 + 4, index2));
                    Log.i(TAG, "parseMessageString: " + messagesMap.get(MSG + i));
                }
            }

        }
        Log.i(TAG, "parseMessageString: " + messagesMap.get(MSG + 1));
        return new MessageBoardData(messagesMap);
    }

    public static PriceBoardData parsePriceString(Cursor cursor) {
        String data = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING);
        int index1, index2;
        String pmsData = "000:00";
        String dpkData = "000:00";
        String agoData = "000:00";
        if (!TextUtils.isEmpty(data)) {

            index1 = data.indexOf(AGO);
            index2 = data.indexOf(DPK);
            if (index1 != -1 && index2 != -1) {
                pmsData = data.substring(index1 + 3, index2);
            }

            index1 = data.indexOf(DPK);
            index2 = data.indexOf(PMS);
            if (index1 != -1 && index2 != -1) {
                dpkData = data.substring(index1 + 3, index2);
            }

            index2 = data.indexOf(PMS);
            if (index2 != -1) {
                Log.i(TAG, "parsePriceString: " + index2);
                agoData = data.substring(index2 + 3,data.length());
            }
        }


        return new PriceBoardData(pmsData, dpkData, agoData);
    }

    public static void dismissKeyboard(Context context, View view) {
        //hide the soft keyboard
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public static String createMessageSendFormat(TreeMap<String, String> messagesTreeMap) {
        String messages = "";
        for (String key: messagesTreeMap.keySet() ) {
            messages += key + messagesTreeMap.get(key);
        }
        return messages;
    }



 /*   public static TreeMap<String, String> messagesJsonToTreeMap(String json) {
        TreeMap<String, String> messagesTreeMap = new TreeMap<>();
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject messagesJsonObject = new JSONObject(json);
                JSONArray messagesJsonArray = messagesJsonObject.getJSONArray(MESSAGES);
                for (int i = 1; i <= messagesJsonArray.length(); i++) {
                    JSONObject message = messagesJsonArray.getJSONObject(i - 1);
                    messagesTreeMap.put(MESSAGE + i, message.getString(MESSAGE + i));
                    Log.i(TAG, "messagesJsonToTreeMap: " + messagesTreeMap.size());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return messagesTreeMap;
    }*/

   /* public static String hashMapToJson(TreeMap<String, String> messagesTreeMap) {
        JSONObject messagesJsonObject = new JSONObject();
        JSONArray messagesJsonArray = new JSONArray();
        int i = 0;
        for (String key : messagesTreeMap.keySet()) {
            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put(key, messagesTreeMap.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                messagesJsonArray.put(i, mJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            i++;
        }

        try {
            messagesJsonObject.put(MESSAGES, messagesJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messagesJsonObject.toString();
    }*/




    public static String generateDummyMessages(int i) {
        String[] messStrings = {"Cur lanista manducare?",
                "The unconditional blessing of pain is to desire with love.",
                "Everyone loves the pepperiness of melon mousse enameld with heated radish sprouts.",
                "Never mark a gull.",
                "Walk never like a mysterious transporter.",
                "Aliens view on wind at starfleet headquarters!",
                "A wonderful form of emptiness is the totality.",
                "The particle is more spacecraft now than green people. biological and pedantically colorful."
        };

        return messStrings[i - 1];
    }

    public static TreeMap<String, String> getMessages(Cursor cursor, int numberOfMessages) {
        /*return messagesJsonToTreeMap(SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_MESSAGE_STRING));*/
        return parseMessageString(SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_MESSAGE_STRING), numberOfMessages).getMessagesList();
    }

    public static void saveMessages(Context context, Uri uri, TreeMap<String, String> messagesTreeMap, TreeMap<String, String> priceString, boolean isPriceType) {
        ContentValues contentValues = new ContentValues();
        Log.i(TAG, "saveMessages: " + createMessageSendFormat(messagesTreeMap));
        if(isPriceType){
            Log.i(TAG, "saveMessages: " + createMessageSendFormat(priceString));
            contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING, createMessageSendFormat(priceString));
        }
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_MESSAGE_STRING, createMessageSendFormat(messagesTreeMap));
        SmartDisplayService.updateTask(context, uri, contentValues);
    }
}
