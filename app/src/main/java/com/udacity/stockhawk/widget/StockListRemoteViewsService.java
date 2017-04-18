package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.FormatUtils;


public class StockListRemoteViewsService extends RemoteViewsService {

    private static final String[] STOCK_WIDGET_COLUMNS = {
            Contract.Quote.COLUMN_NAME,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE
    };

    private static final int INDEX_NAME = 0;
    private static final int INDEX_SYMBOL = 1;
    private static final int INDEX_PRICE = 2;
    private static final int INDEX_PERCENTAGE_CHANGE = 3;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if(data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(Contract.Quote.URI,
                        STOCK_WIDGET_COLUMNS,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL + " ASC");

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stock_list_item);

                String name = data.getString(INDEX_NAME);
                String symbol = data.getString(INDEX_SYMBOL);
                float rawPrice = data.getFloat(INDEX_PRICE);
                float rawPercentChange = data.getFloat(INDEX_PERCENTAGE_CHANGE);

                String price = FormatUtils.getFormatUtils().getDollarFormatUnsigned().format(rawPrice);
                String percentage = FormatUtils.getFormatUtils().getPercentageFormatSigned().format(rawPercentChange / 100);

                views.setTextViewText(R.id.tv_widget_symbol, symbol);
                views.setTextViewText(R.id.tv_widget_price, price);
                views.setTextViewText(R.id.tv_widget_change, percentage);

                String a11yChangeDirection;
                if(rawPercentChange >= 0) {
                    views.setInt(R.id.tv_widget_change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                    a11yChangeDirection = getBaseContext().getString(R.string.a11y_positive_change);
                } else {
                    views.setInt(R.id.tv_widget_change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                    a11yChangeDirection = getBaseContext().getString(R.string.a11y_negative_change);
                }

                String contentDescription = getBaseContext().getString(R.string.a11y_list_item,
                        name,
                        price,
                        a11yChangeDirection,
                        percentage);

                views.setContentDescription(R.id.fl_widget_list_item, contentDescription);

                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(Intent.EXTRA_TEXT, symbol);

                views.setOnClickFillInIntent(R.id.fl_widget_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_stock_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
