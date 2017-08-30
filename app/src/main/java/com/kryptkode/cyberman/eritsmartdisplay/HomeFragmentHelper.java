package com.kryptkode.cyberman.eritsmartdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;
import com.kryptkode.cyberman.eritsmartdisplay.models.PriceBoard;

/**
 * Created by Cyberman on 8/23/2017.
 */

public class HomeFragmentHelper {
    public static final String EXTRA_KEY = "edkg";
   public static void startDetailActivity(Context context, PriceBoard priceBoard){
       Uri uri = SmartDisplayContract.SmartDisplayColumns.buildDisplayUri(priceBoard.getId());
       Intent intent = new Intent(context, DetailActivity.class);
       intent.setData(uri);
       intent.putExtra(EXTRA_KEY, priceBoard);
       context.startActivity(intent);
   }
}
