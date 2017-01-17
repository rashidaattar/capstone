package com.udacity.capstone.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.capstone.R;
import com.udacity.capstone.adapter.LandingAdapter;
import com.udacity.capstone.service.NotificationService;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LandingActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.landing_list)
    RecyclerView recyclerView;

    private InterstitialAd mInterstitialAd;
    private LandingAdapter landingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("CHECK");
        //noinspection deprecation,deprecation
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)//add device id in strings.xml
                .addTestDevice(getString(R.string.test_device_id))
                .build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        scheduleNotification();
        landingAdapter = new LandingAdapter(this,mInterstitialAd);
        recyclerView.setAdapter(landingAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


    }

    public  void scheduleNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        alarmManager.cancel(pendingIntent);

        // Set the alarm to start at approximately 10:00 morning.
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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //add device id in strings.xml
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}
