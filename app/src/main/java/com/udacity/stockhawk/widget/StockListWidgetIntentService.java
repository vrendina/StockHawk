package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailActivity;

import timber.log.Timber;


public class StockListWidgetIntentService extends IntentService {

    public StockListWidgetIntentService() {
        super("StockListWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, StockListWidgetProvider.class));

        for(int widgetId: appWidgetIds) {
            Timber.d("Updating widget with id " + widgetId);

            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_stock_list);

            // Create an Intent to launch MainActivity
            Intent homeIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, homeIntent, 0);
            views.setOnClickPendingIntent(R.id.fl_widget_list_header, pendingIntent);

            // Set up the collection by providing the remote view service
            views.setRemoteAdapter(R.id.lv_widget_list,
                    new Intent(this, StockListRemoteViewsService.class));

            // Set the intent for each list item
            Intent intentTemplate = new Intent(this, StockDetailActivity.class);

            PendingIntent pendingIntentTemplate = TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.lv_widget_list, pendingIntentTemplate);

            views.setEmptyView(R.id.lv_widget_list, R.id.tv_widget_list_empty);

            appWidgetManager.updateAppWidget(widgetId, views);
        }

    }

}
