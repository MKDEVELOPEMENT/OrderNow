package com.example.android.ordernow.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ziyaad B on 2017/07/13.
 */

public class MenuContract {

    private MenuContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.ordernow.data.MenuProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MENU = "menu";

    public static class FeedEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MENU);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MENU;


        public static final String TABLE_NAME = "Menu";
        public static final String COLUMN_NAME_ITEM = "Item";
        public static final String COLUMN_NAME_PRICE = "Price";


    }
}
