package com.example.android.ordernow;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.ordernow.data.MenuContract.FeedEntry;

/**
 * Created by Ziyaad B on 2017/07/14.
 */

public class OrderCursorAdapter extends CursorAdapter {

    public OrderCursorAdapter(Context context, Cursor c){

        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name_menu_item);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        int itemColumnIndex = cursor.getColumnIndex(FeedEntry.COLUMN_NAME_ITEM);
        int priceColumnIndex = cursor.getColumnIndex(FeedEntry.COLUMN_NAME_PRICE);

        String item = cursor.getString(itemColumnIndex);
        double price = cursor.getDouble(priceColumnIndex);
        String strPrice = "R" + String.format("%.2f", price);

        nameTextView.setText(item);
        summaryTextView.setText(strPrice);
    }
}