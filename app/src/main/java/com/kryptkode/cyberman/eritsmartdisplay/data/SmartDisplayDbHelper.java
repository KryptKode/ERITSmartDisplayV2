package com.kryptkode.cyberman.eritsmartdisplay.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplayDbHelper extends SQLiteOpenHelper {

    public static final String TAG = SmartDisplayDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "smart_display.db";
    private static final int DATABASE_VERSION = 1;

    //constants for the SQL commands and syntax
    public static final String CREATE_TABLE = "CREATE TABLE ";
    public static final String COMMA_SEP = ", ";
    public static final String TEXT = " TEXT ";
    public static final String PRIMARY_KEY = " PRIMARY KEY ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String INTEGER = " INTEGER ";
    public static final String REAL = " REAL ";
    public static final String DEFAULT = " DEFAULT ";
    public static final String AUTOINCREMENT = " AUTOINCREMENT ";
    public static final String OPEN_PARENTHESIS = "(";
    public static final String CLOSE_PARENTHESIS = " );";

    public SmartDisplayDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DISPLAY_TABLE = CREATE_TABLE + SmartDisplayColumns.DISPLAY_TABLE_NAME +
                OPEN_PARENTHESIS +
                SmartDisplayColumns._ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
                SmartDisplayColumns.COLUMN_NAME + TEXT + COMMA_SEP +
                SmartDisplayColumns.COLUMN_IP_ADDRESS + TEXT + COMMA_SEP +
                SmartDisplayColumns.COLUMN_BOARD_TYPE + TEXT + COMMA_SEP +
                SmartDisplayColumns.COLUMN_MESSAGE_STRING + TEXT + COMMA_SEP +
                SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING + TEXT + CLOSE_PARENTHESIS;

        Log.i(TAG, "onCreate: " + CREATE_DISPLAY_TABLE);

        db.execSQL(CREATE_DISPLAY_TABLE);
        loadDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SmartDisplayColumns.DISPLAY_TABLE_NAME);
        onCreate(db);
    }

    private void loadDummyData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(SmartDisplayColumns.COLUMN_NAME, "Demo Display");
        values.put(SmartDisplayColumns.COLUMN_IP_ADDRESS, "192.168.43.135");
        values.put(SmartDisplayColumns.COLUMN_BOARD_TYPE, "2-2");
        values.put(SmartDisplayColumns.COLUMN_MESSAGE_STRING, "//M1 Hello world //M2 World is Alive ");
        values.put(SmartDisplayColumns.COLUMN_PRICE_BOARD_STRING, " //P 197 //D 250 //A 150 ");

        db.insertOrThrow(SmartDisplayColumns.DISPLAY_TABLE_NAME, null, values);
    }
}
