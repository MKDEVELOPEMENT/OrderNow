package com.example.android.ordernow;

import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.ordernow.data.MenuContract.FeedEntry;
import com.example.android.ordernow.data.OrderNowDbHelper;
import com.example.android.ordernow.data.OrdersContract.OrdersEntry;
import com.example.android.ordernow.data.OrdersProvider;

public class PlaceOrder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private SQLiteDatabase db;
    PlaceOrderCursorAdapter mCursorAdapter;
    TextView grandTotalTV;
    public static double gt = 0;
    static Menu m;
    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        OrderNowDbHelper mDbHelper = new OrderNowDbHelper(this.getBaseContext());
        db = mDbHelper.getWritableDatabase();

        ListView listView = (ListView) findViewById(R.id.place_list_view);
        mCursorAdapter = new PlaceOrderCursorAdapter(this, null);

        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onBackPressed(){
        gt = 0;
        MenuItem quant = m.findItem(R.id.total_text);
        quant.setTitle("R" + String.format("%.2f", 0d));
        PlaceOrderCursorAdapter.mGrandTotaleMenuTV = 0;
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.place_order_menu, menu);

        m = menu;

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.send_data_menu_button:
                sendDataToCurrentOrders();
                gt = 0;
                MenuItem quant = m.findItem(R.id.total_text);
                quant.setTitle("R" + String.format("%.2f", 0d));
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                gt = 0;
                quant = m.findItem(R.id.total_text);
                quant.setTitle("R" + String.format("%.2f", 0d));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void sendDataToCurrentOrders(){
        View view;
        TextView textView;
        TextView tv;
        TextView itemtv;

        int itemQuantity;
        double itemPrice;

        ContentValues values = new ContentValues();

        String currOrder = "";
        for (int i = 0; i < mCursorAdapter.getCount(); i++){

            view = mCursorAdapter.getView(i, null, null);

            itemtv = (TextView) view.findViewById(R.id.item_name);
            textView = (TextView) view.findViewById(R.id.item_amount_text);
            tv = (TextView) view.findViewById(R.id.item_price);

            itemQuantity = Integer.parseInt(String.valueOf(textView.getText()));
            itemPrice = Double.parseDouble(String.valueOf(tv.getText()));
            String item = String.valueOf(itemtv.getText());

            String newLine = System.getProperty("line.seperator");

            MenuItem menuItem = m.findItem(R.id.total_text);

            String itemOnReceipt = itemQuantity + "x " + item + ":  R" + itemPrice + newLine;

            if (itemQuantity > 0){

                currOrder = currOrder.concat(itemOnReceipt);

            }

        }
        values.put(OrdersEntry.COLUMN_NAME_ORDER_RECEIPT, currOrder);
        values.put(OrdersEntry.COLUMN_NAME_PRICE, gt);
        Uri newUri = getContentResolver().insert(OrdersEntry.CONTENT_URI, values);

        Intent intent = new Intent(PlaceOrder.this, CurrentOrders.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_ITEM,
                FeedEntry.COLUMN_NAME_PRICE
        };

        return new CursorLoader(
                this,
                FeedEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    public static void setMenuItem(double value){
        MenuItem quant = m.findItem(R.id.total_text);
        quant.setTitle("R" + String.format("%.2f", value));
    }
}
