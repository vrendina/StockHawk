package com.udacity.stockhawk;

import android.app.Application;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class StockHawkApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Workaround for redirect issue (#69) in yahoo finance library
        System.setProperty("yahoofinance.baseurl.histquotes", "https://ichart.yahoo.com/table.csv");
        System.setProperty("yahoofinance.baseurl.quotes", "http://download.finance.yahoo.com/d/quotes.csv");

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());

            Stetho.initializeWithDefaults(this);
        }
    }
}
