package com.udacity.capstone.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.udacity.capstone.R;
import com.udacity.capstone.activity.OrderListActivity;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.OrdersTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationService(String name) {
        super(name);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        String myFormat = "dd/MMM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar myCalendar = Calendar.getInstance();
        Cursor mCursor;
        mCursor= getContentResolver().query(InventoryProvider.Orders.ORDERS_URI,
                null, OrdersTable.ORDER_STATUS + " LIKE ?",
                new String[]{OrdersTable.STATUS_PROGRESS}, null);
        if(mCursor.getCount()>0){
            String orderIds = " ";

            while(mCursor.moveToNext()){
                Log.d("test","order id "+mCursor.getString(mCursor.getColumnIndex(OrdersTable.DELIVERY_DATE)));
                if(mCursor.getString(mCursor.getColumnIndex(OrdersTable.DELIVERY_DATE)).equals(sdf.format(myCalendar.getTime())))
                orderIds+=mCursor.getString(mCursor.getColumnIndex(OrdersTable.ORDER_NUMBER));
            }
            if(orderIds!=null && orderIds.length()>0)
            sendNotification(orderIds);
        }

    }

    private void sendNotification(String orderIds) {
        int i = 0;
        Random generator = new Random();
        long[] vibrate = {0, 100, 200, 300};
        Intent intent = new Intent(this, OrderListActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, generator.nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification n = new NotificationCompat.Builder(this)
                        .setContentTitle("Pending Order")
                        .setContentText("The pending orders are : "+orderIds)
                        .setSmallIcon(R.drawable.ic_add_a_photo_black_24dp)
                        .setLargeIcon((((BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.ic_launcher)).getBitmap()))
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setVibrate(vibrate)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .build();
                /*Builder(this)

                .setContentText("You have pending orders to deliver today! "+ "The orders are : ")
                .setSmallIcon(R.drawable.ic_add_a_photo_black_24dp)
                .setLargeIcon((((BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.ic_launcher)).getBitmap()))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setVibrate(vibrate)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX).setWhen(0)
                .build();*/
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(i++, n);
    }
}
