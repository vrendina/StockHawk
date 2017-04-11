package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.FormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetail extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {

    private static final int STOCK_DETAIL_LOADER = 1;
    private String symbol;

    // Contains historical data as price/date
    private Map<Float, String> history;

    @BindView(R.id.tv_companyname)
    TextView companyNameTextView;

    @BindView(R.id.tv_price)
    TextView priceTextView;

    @BindView(R.id.tv_change)
    TextView changeTextView;

    @BindView(R.id.chart_history)
    LineChart chart;

    @BindView(R.id.tv_close)
    TextView closeTextView;

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

            setupHeader(data);
            setupChart(data);

        } else {
            Timber.e("Unexpected error, symbol not found even though passed in intent...");
        }
    }

    private void setupHeader(Cursor data) {
        String name = data.getString(Contract.Quote.POSITION_NAME);
        String ticker = data.getString(Contract.Quote.POSITION_SYMBOL).toUpperCase();

        companyNameTextView.setText(name + " (" + ticker + ")");

        float rawPrice = data.getFloat(Contract.Quote.POSITION_PRICE);
        float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        if(rawAbsoluteChange >= 0) {
            changeTextView.setBackgroundResource(R.drawable.percent_change_pill_green);
        } else {
            changeTextView.setBackgroundResource(R.drawable.percent_change_pill_red);
        }

        String price = FormatUtils.getFormatUtils().getDollarFormatUnsigned().format(rawPrice);
        String change = FormatUtils.getFormatUtils().getDollarFormatSigned().format(rawAbsoluteChange);
        String percentage = FormatUtils.getFormatUtils().getPercentageFormatSigned().format(percentageChange / 100);

        priceTextView.setText(price);
        changeTextView.setText(change + " (" + percentage + ")");
    }

    private void setupChart(Cursor data) {
        // Y-Axis setup
        YAxis left = chart.getAxisLeft();

        left.setDrawGridLines(false);
        left.setDrawLabels(true);
        left.setDrawAxisLine(false);
        left.setLabelCount(6);
        left.setTextColor(Color.LTGRAY);
        left.setTextSize(12);

        left.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return FormatUtils.getFormatUtils().getDollarFormatUnsignedNoDecimals().format(value);
            }
        });

        // X-Axis setup
        XAxis xAxis = chart.getXAxis();

        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawAxisLine(false);

        chart.getAxisRight().setEnabled(false);
        chart.setDescription(null);
        chart.setDrawBorders(false);
        chart.getLegend().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setOnChartValueSelectedListener(this);

        // Setup the data
        List<Entry> entries = new ArrayList<>();
        history = new ArrayMap<>();

        String[] historyArray = data.getString(Contract.Quote.POSITION_HISTORY).split("\\n");

        int i = 0;
        for(String line : historyArray) {
            String[] point = line.split(",");

            String date = DateFormat.getDateFormat(this).format(new Date(Long.parseLong(point[0])));
            Float price = Float.parseFloat(point[1]);

            history.put(price, date);

            entries.add(new Entry(i, price));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setHighLightColor(Color.GRAY);
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setCircleRadius(getResources().getInteger(R.integer.chart_circle_size));
        dataSet.setDrawCircleHole(false);
        dataSet.setFillColor(ContextCompat.getColor(this, R.color.colorPrimary));

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);

        chart.highlightValue(historyArray.length/2, 0, true);
        chart.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String price = FormatUtils.getFormatUtils().getDollarFormatUnsigned().format(e.getY());
        String date = history.get(e.getY());

        String close = getString(R.string.stock_detail_close, price, date);

        closeTextView.setText(close);
        closeTextView.announceForAccessibility(close);
    }

    @Override
    public void onNothingSelected() {

    }
}
