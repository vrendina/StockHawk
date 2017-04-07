package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_DETAIL_LOADER = 1;
    private String symbol;

    @BindView(R.id.tv_companyname)
    TextView companyNameTextView;

    @BindView(R.id.tv_price)
    TextView priceTextView;

    @BindView(R.id.tv_change)
    TextView changeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        ButterKnife.bind(this);

        symbol = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        // Start a cursor loader to fetch data from the content provider
        getSupportLoaderManager().initLoader(STOCK_DETAIL_LOADER, null, this);
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

            String name = data.getString(Contract.Quote.POSITION_NAME);
            String ticker = data.getString(Contract.Quote.POSITION_SYMBOL).toUpperCase();

            companyNameTextView.setText(name + " (" + ticker + ")");

            float rawPrice = data.getFloat(Contract.Quote.POSITION_PRICE);
            float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

            if(rawAbsoluteChange >= 0) {
                //changeTextView.setTextColor(ContextCompat.getColor(this, R.color.material_green_700));
                changeTextView.setBackgroundResource(R.drawable.percent_change_pill_green);
            } else {
                changeTextView.setBackgroundResource(R.drawable.percent_change_pill_red);
            }

            String price = FormatUtils.getDollarFormatUnsigned().format(rawPrice);
            String change = FormatUtils.getDollarFormatSigned().format(rawAbsoluteChange);
            String percentage = FormatUtils.getPercentageFormatSigned().format(percentageChange / 100);

            priceTextView.setText(price);
            changeTextView.setText(change + " (" + percentage + ")");

        } else {
            Timber.e("Unexpected error, symbol not found even though passed in intent...");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
