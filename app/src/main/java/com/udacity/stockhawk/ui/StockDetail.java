package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import timber.log.Timber;

public class StockDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_DETAIL_LOADER = 1;
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        symbol = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        // Start a cursor loader to fetch data from the content provider
        getSupportLoaderManager().initLoader(STOCK_DETAIL_LOADER, null, this);

        //Contract.Quote.makeUriForStock(symbol)
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Quote.makeUriForStock(symbol),
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() > 0) {
            data.moveToFirst();
        } else {
            Timber.e("Unexpected error, symbol not found even though passed in intent...");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
