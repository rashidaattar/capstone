package com.udacity.capstone.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.database.ProductTable;
import com.udacity.capstone.service.NotificationService;
import com.udacity.capstone.util.Constants;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ComponentName mServiceComponent;

    private int mJobId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("CHECK");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        scheduleNotification();
    }

    @OnClick(R.id.prod_button)
    public void prodClick(){
        Intent intent=new Intent(this,ProductListActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.order_button)
    public void orderClick(){
        Intent intent = new Intent(this,OrderListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.customer_button)
    public void viewCustomers(){
        Intent intent=new Intent(this,CustomerListActivity.class);
        intent.putExtra(Constants.PERSON_TYPE_LABEL,Constants.CUSTOMER_TYPE);
        startActivity(intent);
    }

    @OnClick(R.id.vendor_button)
    public void viewVendors(){
        Intent intent=new Intent(this,CustomerListActivity.class);
        intent.putExtra(Constants.PERSON_TYPE_LABEL,Constants.VENDOR_TYPE);
        startActivity(intent);
    }

    public  void scheduleNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        // reset previous pending intent
        alarmManager.cancel(pendingIntent);

        // Set the alarm to start at approximately 08:00 morning.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // if the scheduler date is passed, move scheduler time to tomorrow
        if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
