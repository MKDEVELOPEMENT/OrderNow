package com.example.android.ordernow;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.ordernow.data.MenuContract.FeedEntry;

/**
 * Created by muaaz on 2017/07/18.
 */

public class PlaceOrderCursorAdapter extends CursorAdapter {

    public PlaceOrderCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.place_order_list_view, parent, false);
    }

    int newmount;
    public static double totalPrice;
    public static double mGrandTotaleMenuTV;

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView itemtv = (TextView) view.findViewById(R.id.item_name);
        TextView pricetv = (TextView) view.findViewById(R.id.item_price);
        final TextView total = (TextView) view.findViewById(R.id.semi_total_text);

        int itemColumnIndex = cursor.getColumnIndex(FeedEntry.COLUMN_NAME_ITEM);
        int priceColumnIndex = cursor.getColumnIndex(FeedEntry.COLUMN_NAME_PRICE);

        String item = cursor.getString(itemColumnIndex);
        final double dprice = cursor.getDouble(priceColumnIndex);
        String price = String.valueOf(cursor.getDouble(priceColumnIndex));

        itemtv.setText(item);
        pricetv.setText(price);

        final TextView itemAMNTTV = (TextView) view.findViewById(R.id.item_amount_text);
        Button addItem = (Button) view.findViewById(R.id.add_item_button);
        Button removeItem = (Button) view.findViewById(R.id.remove_item_button);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newmount = Integer.parseInt(String.valueOf(itemAMNTTV.getText())) + 1;
                itemAMNTTV.setText(String.valueOf("" + newmount));
                totalPrice = (newmount * dprice);
                mGrandTotaleMenuTV += dprice;
                total.setText("R" + String.format("%.2f", totalPrice));
                PlaceOrder.gt = mGrandTotaleMenuTV;
                PlaceOrder.setMenuItem(PlaceOrder.gt);
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(String.valueOf(itemAMNTTV.getText())) > 0){
                    newmount = Integer.parseInt(String.valueOf(itemAMNTTV.getText())) - 1;
                    itemAMNTTV.setText(String.valueOf("" + newmount));
                    totalPrice = (newmount * dprice);
                    mGrandTotaleMenuTV -= dprice;
                    total.setText("R" + String.format("%.2f", totalPrice));
                    PlaceOrder.gt = mGrandTotaleMenuTV;
                    PlaceOrder.setMenuItem(PlaceOrder.gt);
                }
            }
        });
    }
}