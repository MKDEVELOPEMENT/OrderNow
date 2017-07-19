package com.example.android.ordernow.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by muaaz on 2017/07/19.
 */

public class OrdersContract {
    private OrdersContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.ordernow";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ORDERS = "orders";

    public static class OrdersEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ORDERS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDERS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDERS;


        public static final String TABLE_NAME = "Orders";
        public static final String COLUMN_NAME_ITEM = "CurrentOrder";
        public static final String COLUMN_NAME_PRICE = "TotalPrice";

    }
}
