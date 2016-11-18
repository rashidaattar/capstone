package com.udacity.capstone.activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udacity.capstone.R;
import com.udacity.capstone.adapter.ViewPagerAdapter;
import com.udacity.capstone.fragment.AddressInfoTabFragment;
import com.udacity.capstone.fragment.CustomerInfoTabFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditCustomerActivity extends AppCompatActivity implements AddressInfoTabFragment.OnFragmentInteractionListener,CustomerInfoTabFragment.OnFragmentInteractionListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_customer);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CustomerInfoTabFragment(), "ONE");
        adapter.addFragment(new AddressInfoTabFragment(), "TWO");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
