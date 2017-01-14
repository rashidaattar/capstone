package com.udacity.capstone.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.OrdersTable;

/**
 * Created by Rashida on 02-01-2017.
 */

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;

    public WidgetRemoteViewFactory(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public void onCreate() {
       /* mCursor= mContext.getContentResolver().query(InventoryProvider.Orders.ORDERS_URI,
                null, OrdersTable.ORDER_STATUS + " LIKE ?",
                new String[]{OrdersTable.STATUS_PROGRESS}, null);
        for(int i=0;i<mCursor.getCount();i++){
            getCount();
            getViewAt(i);
        }
*/
    }

    @Override
    public void onDataSetChanged() {
        mCursor= mContext.getContentResolver().query(InventoryProvider.Orders.ORDERS_URI,
                null, OrdersTable.ORDER_STATUS + " LIKE ?",
                new String[]{OrdersTable.STATUS_PROGRESS}, null);
        for(int i=0;i<mCursor.getCount();i++){
            getCount();
            getViewAt(i);
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.order_number,mCursor.getString(mCursor.getColumnIndex(OrdersTable.ORDER_NUMBER)));
        rv.setTextViewText(R.id.order_amount,mContext.getString(R.string.order_number_widget,mCursor.getString(mCursor.getColumnIndex(OrdersTable.ORDER_AMOUNT))));
        rv.setTextViewText(R.id.delivery_date,mCursor.getString(mCursor.getColumnIndex(OrdersTable.DELIVERY_DATE)));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, position);
        rv.setOnClickFillInIntent(R.id.order_number, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
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
        return false;
    }
}
