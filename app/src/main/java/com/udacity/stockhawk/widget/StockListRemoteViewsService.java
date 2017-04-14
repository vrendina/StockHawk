package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class StockListRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }
}
