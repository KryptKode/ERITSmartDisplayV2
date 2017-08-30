package com.kryptkode.cyberman.eritsmartdisplay;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

import static com.kryptkode.cyberman.eritsmartdisplay.DetailFragment.TAG;

/**
 * Created by Cyberman on 8/25/2017.
 */

public class DetailFragmentHelper {

    public static void dismissKeyboard(Context context, View view) {
        //hide the soft keyboard
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
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

        return messStrings[i - 1];
    }

    public static void saveMessages(Context context, PriceBoard priceBoard) {
        ContentValues contentValues = new ContentValues();
        Log.i(TAG, "saveMessages: " + priceBoard.createMessageSendFormat());
        if (priceBoard.getPriceBoardType() == PriceBoard.PriceBoardType.PRICE_BOARD_TYPE_NONE) { //the display is a mesage board
            Log.i(TAG, "saveMessages: " + priceBoard.createPriceSendFormat());
            contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING, priceBoard.createPriceSendFormat());
        }
        contentValues.put(SmartDisplayContract.SmartDisplayColumns.COLUMN_MESSAGE_STRING, priceBoard.createMessageSendFormat());
        SmartDisplayService.updateTask(context, priceBoard.buildBoardUri(), contentValues);
    }

    public static void displayLoadingIndicatiors(View[] views, boolean shouldDisplay) {
        for (View view : views) {
            if(shouldDisplay){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.INVISIBLE);
            }
        }
    }
}
