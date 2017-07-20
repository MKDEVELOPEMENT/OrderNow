package com.example.android.ordernow;

import android.app.LoaderManager;
import android.content.ClipData;
import android.content.CursorLoader;
import android.content.Loader;
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

import org.w3c.dom.Text;

public class PlaceOrder extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private SQLiteDatabase db;
    PlaceOrderCursorAdapter mCursorAdapter;
    TextView grandTotalTV;
    public static double gt;
    static Menu m;

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    int testingComit;

    public void sendDataToCurrentOrders(){
        View view;
        TextView textView;
        TextView tv;
        int itemQuantity;
        for (int i = 0; i < mCursorAdapter.getCount(); i++){
            view = mCursorAdapter.getView(i, null, null);
            textView = (TextView) view.findViewById(R.id.item_amount_text);
            tv = (TextView) view.findViewById(R.id.item_price);
            itemQuantity = Integer.parseInt(String.valueOf(textView));
        }
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
        MenuItem quant = (MenuItem) m.findItem(R.id.total_text);
        quant.setTitle("R" + String.format("%.2f", value));
    }
}
