package com.kryptkode.cyberman.eritsmartdisplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;

/**
 * Created by Cyberman on 8/23/2017.
 */

public class HomeFragmentHelper {

   public static void startDetailActivity(Context context, long id){
       Uri uri = SmartDisplayContract.SmartDisplayColumns.buildDisplayUri(id);
       Intent intent = new Intent(context, DetailActivity.class);
       intent.setData(uri);
       context.startActivity(intent);
   }
}
