package com.udacity.capstone.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.OrdersTable;

/**
 * Created by 836137 on 02-01-2017.
 */

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;

    public WidgetRemoteViewFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

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
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.stock_symbol,mCursor.getString(mCursor.getColumnIndex(OrdersTable.DELIVERY_DATE)));
        rv.setTextViewText(R.id.bid_price,mCursor.getString(mCursor.getColumnIndex(OrdersTable.ORDER_AMOUNT)));
        rv.setTextViewText(R.id.change,mCursor.getString(mCursor.getColumnIndex(OrdersTable.ORDER_STATUS)));
      /*  Intent fillInIntent = new Intent();
        fillInIntent.putExtra(mContext.getString(R.string.symbole_label),mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
        rv.setOnClickFillInIntent(R.id.stock_symbol, fillInIntent);
*/
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
