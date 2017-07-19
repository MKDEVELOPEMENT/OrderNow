package com.example.android.ordernow;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.ordernow.data.MenuContract.FeedEntry;

public class EditMenu extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    EditText itemEdit;
    EditText priceEdit;
    Uri currentMenuItemUri;
    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        itemEdit = (EditText) findViewById(R.id.item_id);
        priceEdit = (EditText) findViewById(R.id.price_id);

        Intent intent = getIntent();

        currentMenuItemUri = intent.getData();

        if (currentMenuItemUri == null){
            setTitle("Create Item");
           invalidateOptionsMenu();
        }else {
            setTitle("Edit Item");
            getLoaderManager().initLoader(0, null, this);
        }

        itemEdit.setOnTouchListener(mTouchListener);
        priceEdit.setOnTouchListener(mTouchListener);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        if (currentMenuItemUri == null){
            MenuItem item = menu.findItem(R.id.do_delete);
            item.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.save:
                SaveInfo();
                finish();
                return true;
            case R.id.do_delete:
                showDeleteConfirmationDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("delete current MenuAct Item ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMenuItem();
            }
        });
        builder.setNegativeButton("keep", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void SaveInfo(){

        String actitem = String.valueOf(itemEdit.getText());

        double price;

        if (!TextUtils.isEmpty(priceEdit.getText())){
            price = Double.parseDouble(String.valueOf(priceEdit.getText()));
        }else{
            price = 0;
        }

        if (currentMenuItemUri == null && TextUtils.isEmpty(actitem)){
            return;
        }

        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ITEM, actitem);
        values.put(FeedEntry.COLUMN_NAME_PRICE, price);

        if (currentMenuItemUri == null){
            Uri newUri = getContentResolver().insert(FeedEntry.CONTENT_URI, values);
        }else{
            int rowsAffected = getContentResolver().update(currentMenuItemUri, values, null, null);

            if(rowsAffected == 0){
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
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
                currentMenuItemUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(FeedEntry.COLUMN_NAME_ITEM);
            int priceColumnIndex = cursor.getColumnIndex(FeedEntry.COLUMN_NAME_PRICE);

            String itemName = cursor.getString(nameColumnIndex);
            Double itemPrice = cursor.getDouble(priceColumnIndex);

            itemEdit.setText(itemName);
            priceEdit.setText(itemPrice.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemEdit.setText("");
        priceEdit.setText("");
    }

    public void deleteMenuItem(){
        if (currentMenuItemUri != null){
            int rowsDeleted = getContentResolver().delete(currentMenuItemUri, null, null);

            if (rowsDeleted == 0){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
