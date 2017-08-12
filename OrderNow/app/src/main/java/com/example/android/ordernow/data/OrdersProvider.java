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

import com.example.android.ordernow.data.OrdersContract.OrdersEntry;

/**
 * Created by muaaz on 2017/07/19.
 */

public class OrdersProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = OrdersProvider.class.getSimpleName();

    public int tester;

    /** URI matcher code for the content URI for the pets table */
    private static final int ORDERS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int ORDERS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(OrdersContract.CONTENT_AUTHORITY, OrdersContract.PATH_ORDERS, ORDERS);

        sUriMatcher.addURI(OrdersContract.CONTENT_AUTHORITY, OrdersContract.PATH_ORDERS + "/#", ORDERS_ID);
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
            case ORDERS:

                cursor = database.query(
                        OrdersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case ORDERS_ID:

                selection = OrdersEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(
                        OrdersEntry.TABLE_NAME,
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
            case ORDERS:
                return insertOrder(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertOrder(Uri uri, ContentValues values){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String order = values.getAsString(OrdersEntry.COLUMN_NAME_ORDER_RECEIPT);
        if (order == null){
            throw new IllegalArgumentException("there has to be a valid order");
        }

        long id = database.insert(
                OrdersContract.OrdersEntry.TABLE_NAME,
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
            case ORDERS:
                rowsDeleted = database.delete(MenuContract.FeedEntry.TABLE_NAME, selection, args);
                break;
            case ORDERS_ID:
                selection = MenuContract.FeedEntry._ID + " =?";
                args = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MenuContract.FeedEntry.TABLE_NAME, selection, args);
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
            case ORDERS:
                return updateMenu(uri, contentValues, s, strings);
            case ORDERS_ID:
                s = MenuContract.FeedEntry._ID + " =?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateMenu(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateMenu(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(MenuContract.FeedEntry.COLUMN_NAME_ITEM)){
            String item = values.getAsString(MenuContract.FeedEntry.COLUMN_NAME_ITEM);
            if (item == null){
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(MenuContract.FeedEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ORDERS:
                return OrdersEntry.CONTENT_LIST_TYPE;
            case ORDERS_ID:
                return OrdersEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}