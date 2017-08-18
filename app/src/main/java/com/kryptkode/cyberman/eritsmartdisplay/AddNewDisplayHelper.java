package com.kryptkode.cyberman.eritsmartdisplay;

import android.view.View;

/**
 * Created by Cyberman on 8/15/2017.
 */

public class AddNewDisplayHelper {




    public static void toggleViewVisibility(View [] views, boolean shouldShow){

        for (View view :views) {

            if (shouldShow){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }
        }
    }
}
