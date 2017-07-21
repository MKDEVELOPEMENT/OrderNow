package com.example.android.ordernow;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.android.ordernow.data.OrderNowDbHelper;
import com.example.android.ordernow.data.OrdersContract.OrdersEntry;

public class CurrentOrders extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private SQLiteDatabase db;
    CurrentOrdersCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        OrderNowDbHelper mDbHelper = new OrderNowDbHelper(this.getBaseContext());
        db = mDbHelper.getWritableDatabase();

        ListView listView = (ListView) findViewById(R.id.current_orders_lview);
        mCursorAdapter = new CurrentOrdersCursorAdapter(this, null);

        listView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                OrdersEntry._ID,
                OrdersEntry.COLUMN_NAME_ORDER_RECEIPT,
                OrdersEntry.COLUMN_NAME_PRICE
        };

        return new CursorLoader(
                this,
                OrdersEntry.CONTENT_URI,
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
}
