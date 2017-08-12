package com.example.android.ordernow.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.ordernow.data.MenuContract.FeedEntry;

/**
 * Created by muaaz on 2017/07/16.
 */

public class MenuProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = OrdersProvider.class.getSimpleName();

    public int tester;

    /** URI matcher code for the content URI for the pets table */
    private static final int MENUS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int MENU_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(MenuContract.CONTENT_AUTHORITY, MenuContract.PATH_MENU, MENUS);

        sUriMatcher.addURI(MenuContract.CONTENT_AUTHORITY, MenuContract.PATH_MENU + "/#", MENU_ID);
    }

    /** Database helper object */
    private OrderNowDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new OrderNowDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case MENUS:

                cursor = database.query(
                        FeedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MENU_ID:

                selection = FeedEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(
                        FeedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MENUS:
                return insertMenuItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMenuItem(Uri uri, ContentValues values){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String itemName = values.getAsString(FeedEntry.COLUMN_NAME_ITEM);
        if (itemName == null){
            throw new IllegalArgumentException("item has to have a name");
        }

        long id = database.insert(
                FeedEntry.TABLE_NAME,
                null,
                values
        );

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] args) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case MENUS:
                rowsDeleted = database.delete(FeedEntry.TABLE_NAME, selection, args);
                break;
            case MENU_ID:
                selection = FeedEntry._ID + " =?";
                args = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(FeedEntry.TABLE_NAME, selection, args);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MENUS:
                return updateMenu(uri, contentValues, s, strings);
            case MENU_ID:
                s = FeedEntry._ID + " =?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateMenu(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateMenu(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(FeedEntry.COLUMN_NAME_ITEM)){
            String item = values.getAsString(FeedEntry.COLUMN_NAME_ITEM);
            if (item == null){
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(FeedEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MENUS:
                return FeedEntry.CONTENT_LIST_TYPE;
            case MENU_ID:
                return FeedEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
