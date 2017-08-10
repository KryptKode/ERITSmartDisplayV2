package com.kryptkode.cyberman.eritsmartdisplay.data;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplayContract {
    public static final String AUTHORITY = "com.kryptkode.cyberman.eritsmartdisplay.data";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static class SmartDisplayColumns implements BaseColumns{
        public static final String DISPLAY_TABLE_NAME = "display_table";

        public static final Uri DISPLAY_CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(DISPLAY_TABLE_NAME).build();


        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IP_ADDRESS = "ip_address";
        public static final String COLUMN_BOARD_TYPE = "board_type";
        public static final String COLUMN_MESSAGE_STRING = "message_string";
        public static final String COLUMN_PRICE_BOARD_STRING = "price_string";

        public static Uri buildDisplayUri(long id) {
           return ContentUris.withAppendedId(BASE_URI, id);
        }
    }

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }


    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
