package com.example.android.ordernow.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by muaaz on 2017/07/19.
 */

public class OrdersProvider extends ContentProvider {

    public static final String LOG_TAG = MenuProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int ORDERS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int ORDERS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(MenuContract.CONTENT_AUTHORITY, MenuContract.PATH_MENU, ORDERS);

        sUriMatcher.addURI(MenuContract.CONTENT_AUTHORITY, MenuContract.PATH_MENU + "/#", ORDERS_ID);
    }

    /** Database helper object */
    private OrderNowDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new OrderNowDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
