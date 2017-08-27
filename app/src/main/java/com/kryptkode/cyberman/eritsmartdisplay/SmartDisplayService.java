package com.kryptkode.cyberman.eritsmartdisplay;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract;

/**
 * Created by Cyberman on 8/18/2017.
 */

public class SmartDisplayService extends IntentService {
    public static final String TAG  = SmartDisplayService.class.getSimpleName();

    public static final String ACTION_UPDATE = "com.kryptkode.cyberman.eritsmartdisplay.UPDATE";
    public static final String ACTION_INSERT = "com.kryptkode.cyberman.eritsmartdisplay.INSERT";
    public static final String ACTION_DELETE = "com.kryptkode.cyberman.eritsmartdisplay.DELETE";
    private static final String EXTRA_VALUES = TAG + ".ContentValues";
    public static final String ACTION_READ_DB = TAG + ".ReadDatabase";
    public static final String DISPLAY_PAYLOAD = "diaply_payload";

    private static Context c;

    public SmartDisplayService() {
        super(TAG);
    }

    public static void insertNewTask(Context context, ContentValues values) {
        Intent intent = new Intent(context, SmartDisplayService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void updateTask(Context context, Uri uri, ContentValues values) {
        Intent intent = new Intent(context, SmartDisplayService.class);
        intent.setAction(ACTION_UPDATE);
        intent.setData(uri);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }


    public static void deleteTask(Context context, Uri uri) {
        Intent intent = new Intent(context, SmartDisplayService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        c = context;
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_INSERT.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        } else if (ACTION_UPDATE.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performUpdate(intent.getData(), values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            performDelete(intent.getData());
        }
    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(SmartDisplayContract.SmartDisplayColumns.DISPLAY_CONTENT_URI, values) != null) {
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Inserted new task");
            sendData(true);
        } else {
            Log.w(TAG, "Error inserting new task");
        }
    }

    private void performUpdate(Uri uri, ContentValues values) {
        int count = getContentResolver().update(uri, values, null, null);
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        sendData(true);
        Log.d(TAG, "Updated " + count + " task items");
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);
        Toast.makeText(c, "Deleted", Toast.LENGTH_LONG).show();
        sendData(true);
        Log.d(TAG, "Deleted " + count + " tasks");
    }

    private void sendData(boolean success) {
        Log.i(TAG, "sendData: " + success);
        Intent intent = new Intent(ACTION_READ_DB);
        intent.putExtra(DISPLAY_PAYLOAD, success);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(intent);
    }
}
