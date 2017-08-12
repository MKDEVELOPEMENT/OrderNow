package com.example.android.ordernow;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.ordernow.data.MenuContract.FeedEntry;
import com.example.android.ordernow.data.OrderNowDbHelper;

public class MenuAct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    OrderCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        OrderNowDbHelper dbHelper = new OrderNowDbHelper(this.getBaseContext());

        ListView menuListView = (ListView) findViewById(R.id.ListView);

        mCursorAdapter = new OrderCursorAdapter(this, null);
        menuListView.setAdapter(mCursorAdapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MenuAct.this, EditMenu.class);

                Uri currentMenuItemUri = ContentUris.withAppendedId(FeedEntry.CONTENT_URI, id);
                intent.setData(currentMenuItemUri);

                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getLoaderManager().initLoader(0, null, this);
    }

    public void plusButton (View view){
        Intent intent = new Intent(MenuAct.this, EditMenu.class);
        startActivity(intent);
    }

    private void deleteAllItems(){
        int rowsDeleted = getContentResolver().delete(FeedEntry.CONTENT_URI, null, null);
        Toast.makeText(this, rowsDeleted + "rows deleted", Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_all_item:
                deleteAllItems();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
