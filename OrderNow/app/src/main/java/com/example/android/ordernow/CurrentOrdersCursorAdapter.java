package com.example.android.ordernow;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.ordernow.data.OrdersContract.OrdersEntry;

/**
 * Created by muaaz on 2017/07/20.
 */

public class CurrentOrdersCursorAdapter extends CursorAdapter{

    public CurrentOrdersCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.current_orders_list_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView idtv = (TextView) view.findViewById(R.id.order_reference);
        TextView totalDuetv = (TextView) view.findViewById(R.id.order_price);

        int idColumnIndex = cursor.getColumnIndex(OrdersEntry._ID);
        int totalColumnIndex = cursor.getColumnIndex(OrdersEntry.COLUMN_NAME_PRICE);

        String id = cursor.getString(idColumnIndex);
        String totalDue = String.valueOf(cursor.getDouble(totalColumnIndex));

        idtv.setText(id);
        totalDuetv.setText(totalDue);

    }
}
