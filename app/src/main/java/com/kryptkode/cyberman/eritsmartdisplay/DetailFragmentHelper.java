package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoard;
import com.kryptkode.cyberman.eritsmartdisplay.models.MessageBoardData;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoardData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.kryptkode.cyberman.eritsmartdisplay.DetailFragment.TAG;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class DetailFragmentHelper {
    public static final String PMS = "//P";
    public static final String DPK = "//D";
    public static final String AGO = "//A";
    public static final String MSG = "//M";
    public static final String MESSAGE = "message";
    public static final String MESSAGES = "messages";


    public static void parseCursor(Cursor cursor) {
        String messages = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_MESSAGE_STRING);
        String prices = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING);

    }

    private MessageBoardData parseMessageString(String data, int numOfMessagees) {

        return new MessageBoardData();
    }

    public static PriceBoardData parsePriceString(Cursor cursor) {
        String data = SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING);
        int index1, index2;
        String pmsData = "000:00";
        String dpkData = "000:00";
        String agoData = "000:00";

        index1 = data.indexOf(PMS);
        index2 = data.indexOf(DPK);
        if (index1 != -1 && index2 != -1) {
            pmsData = data.substring(index1 + 3, index2);
        }

        index1 = data.indexOf(DPK);
        index2 = data.indexOf(AGO);
        if (index1 != -1 && index2 != -1) {
            dpkData = data.substring(index1 + 3, index2);
        }

        index2 = data.indexOf(AGO);
        if (index2 != -1) {
            agoData = data.substring(index2 + 3, index2 + 10);
        }


        return new PriceBoardData(pmsData, dpkData, agoData);
    }

    public static void dismissKeyboard(Context context, View view) {
        //hide the soft keyboard
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public static String createSendFormat(HashMap<String, String> messagesHashMap) {
        String messages = "";
        for (int i = 1; i <= messagesHashMap.size(); i++) {
            messages += messages + MSG + i + " " + messagesHashMap.get(MESSAGE + i);
        }
        return messages;
    }

    public static HashMap<String, String> jsonToHashMap(String json) {
        HashMap<String, String> messagesHashMap = new HashMap<>();

        try {
            JSONObject messagesJsonObject = new JSONObject(json);
            JSONArray messagesJsonArray = messagesJsonObject.getJSONArray(MESSAGES);
            for (int i = 1; i <= messagesJsonArray.length(); i++) {
                JSONObject message = messagesJsonArray.getJSONObject(i - 1);
                messagesHashMap.put(MESSAGE + i, message.getString(MESSAGE + i));
                Log.i(TAG, "jsonToHashMap: " + messagesHashMap.size());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messagesHashMap;
    }

    public static String hashMapToJson(HashMap<String, String> messagesHashMap) {
        JSONObject messagesJsonObject = new JSONObject();
        JSONArray messagesJsonArray = new JSONArray();
        for (String key : messagesHashMap.keySet()) {
            JSONObject mJsonObject = new JSONObject();
            try {
                mJsonObject.put(key, messagesHashMap.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            messagesJsonArray.put(mJsonObject);
        }

        try {
            messagesJsonObject.put(MESSAGES, messagesJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messagesJsonObject.toString();
    }

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

        return messStrings[i-1];
    }

    public static HashMap<String, String> getMessages(Cursor cursor){
        return jsonToHashMap(SmartDisplayContract.getColumnString(cursor,
                SmartDisplayContract.SmartDisplayColumns.COLUMN_MESSAGE_STRING));
    }
}
