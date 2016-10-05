package com.lakesidestudio.apphelper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lakesidestudio.apphelper.util.DeviceManagerUtil;

/**
 * Implementation of App Widget functionality.
 */
public class QuickButtonAppWidget extends AppWidgetProvider {
    private static final String CLICK_LOCK = "click.lock.action";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quick_button_app_widget);
        views.setImageViewResource(R.id.imageView, android.R.drawable.ic_lock_lock);

        views.setOnClickPendingIntent(R.id.imageView, getPendingSelfIntent(context, CLICK_LOCK));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, QuickButtonAppWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case CLICK_LOCK:
                DeviceManagerUtil.lock(context);
                break;
            default:
                super.onReceive(context,intent);
        }
    }
}

