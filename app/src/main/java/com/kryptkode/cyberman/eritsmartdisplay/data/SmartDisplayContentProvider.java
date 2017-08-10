package com.kryptkode.cyberman.eritsmartdisplay.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.kryptkode.cyberman.eritsmartdisplay.data.SmartDisplayContract.SmartDisplayColumns;

/**
 * Created by Cyberman on 8/9/2017.
 */

public class SmartDisplayContentProvider extends ContentProvider {
    private SmartDisplayDbHelper dbHelper;
    public static final int ALL_DISPLAYS = 100;
    public static final int ONE_DISPLAY = 101;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(SmartDisplayContract.AUTHORITY,
                SmartDisplayColumns.DISPLAY_TABLE_NAME, ALL_DISPLAYS);

        sUriMatcher.addURI(SmartDisplayContract.AUTHORITY, SmartDisplayColumns.DISPLAY_TABLE_NAME + "/#", ONE_DISPLAY);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new SmartDisplayDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case ONE_DISPLAY:
                builder.appendWhere(SmartDisplayColumns._ID + "=" + uri.getLastPathSegment());
            case ALL_DISPLAYS:
                builder.setTables(SmartDisplayColumns.DISPLAY_TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri" + uri.toString());
        }
        Cursor cursor = builder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //not used
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (values == null) {
            throw new IllegalArgumentException("ContentValues should not be null");
        }
        switch (sUriMatcher.match(uri)) {
            case ALL_DISPLAYS:
                long id = dbHelper.getWritableDatabase().insert(SmartDisplayColumns.DISPLAY_TABLE_NAME,
                        null, values);
                if (id >0){
                    getContext().getContentResolver().notifyChange(uri, null);
                    return SmartDisplayColumns.buildDisplayUri(id);
                }else{
                    throw new SQLException("Failed to insert into " + uri);
                }
            default:
                throw new IllegalArgumentException("Unknown uri "+ uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (sUriMatcher.match(uri)){
            case ONE_DISPLAY:
                long id = ContentUris.parseId(uri);
                selection = SmartDisplayColumns._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            case ALL_DISPLAYS:
                selection = (selection == null) ? "1" : selection;
                break;
            default:
                throw new IllegalArgumentException("Unknown uri "+ uri.toString());
        }

       int numOfRowsDeleted = dbHelper.getWritableDatabase().
               delete(SmartDisplayColumns.DISPLAY_TABLE_NAME, selection, selectionArgs);
        if (numOfRowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)){
            case ONE_DISPLAY:
                selection = SmartDisplayColumns._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                throw new IllegalArgumentException("Unknown uri "+ uri.toString());
        }

        int count = dbHelper.getWritableDatabase().update(SmartDisplayColumns.DISPLAY_TABLE_NAME,
                values, selection, selectionArgs);
        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
