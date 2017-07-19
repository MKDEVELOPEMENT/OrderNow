package com.example.android.ordernow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ziyaad B on 2017/07/13.
 */
import com.example.android.ordernow.data.MenuContract.FeedEntry;
import com.example.android.ordernow.data.OrdersContract.OrdersEntry;

public class OrderNowDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OrderNow.db";

    private static final int DATABASE_VERSION = 1;

    public OrderNowDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MENU_TABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME + "(" +
                FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FeedEntry.COLUMN_NAME_ITEM + " TEXT NOT NULL, " +
                FeedEntry.COLUMN_NAME_PRICE + " REAL NOT NULL DEFAULT 0);";
        db.execSQL(CREATE_MENU_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE" + OrdersEntry.TABLE_NAME + "(" +
                OrdersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OrdersEntry.COLUMN_NAME_ITEM + " TEXT NOT NULL, " +
                OrdersEntry.COLUMN_NAME_PRICE + " REAL NOT NULL DEFAULT 0);";
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
