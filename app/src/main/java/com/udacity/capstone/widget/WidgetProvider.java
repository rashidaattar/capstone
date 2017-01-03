package com.udacity.capstone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.capstone.R;
import com.udacity.capstone.activity.LandingActivity;
import com.udacity.capstone.activity.OrderListActivity;

/**
 * Created by 836137 on 02-01-2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(R.id.list1, intent);
            Intent orderListIntent = new Intent(context, OrderListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, orderListIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.list1, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list1);
           /* Intent historyIntent = new Intent(context, LandingActivity.class);
            historyIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent toastPendingIntent = PendingIntent.getActivity(context, 0, historyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.list1, toastPendingIntent);*/
           /* Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.list1, intent);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            Intent historyIntent = new Intent(context, LandingActivity.class);
            historyIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent toastPendingIntent = PendingIntent.getActivity(context, 0, historyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.list1, toastPendingIntent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list1);*/

        }
    }
}
