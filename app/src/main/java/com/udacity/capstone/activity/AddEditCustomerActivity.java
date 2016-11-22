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
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditCustomerActivity extends AppCompatActivity implements AddressInfoTabFragment.OnFragmentInteractionListener,CustomerInfoTabFragment.OnFragmentInteractionListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public String personID;

    public String personIDEdit;
    public String addressIDEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_customer);
        ButterKnife.bind(this);
        if(getIntent()!=null){ //coming from edit action mode
            if(getIntent().hasExtra(Constants.EDIT_CUSTOMER_BOOLEAN) && getIntent().getBooleanExtra(Constants.EDIT_CUSTOMER_BOOLEAN,false)){

                personIDEdit = getIntent().getStringExtra(Constants.EDIT_CUSTOMER_CUSTOMERID);
                addressIDEdit = getIntent().getStringExtra(Constants.EDIT_CUSTOMER_ADDRESSID);
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(getIntent().getBooleanExtra(Constants.EDIT_CUSTOMER_BOOLEAN,false)){
            adapter.addFragment(CustomerInfoTabFragment.newInstance(personIDEdit), "ONE");
            adapter.addFragment(AddressInfoTabFragment.newInstance(addressIDEdit,personIDEdit), "TWO");
        }
        else{
            adapter.addFragment(new CustomerInfoTabFragment(),"ONE");
            adapter.addFragment(new AddressInfoTabFragment(),"TWO");
        }

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onFragmentInteractionCustomer(Uri uri) {

        uri.getPath().toString();
        String id[] = uri.getPath().toString().split("/");
        personID =id[2];

    }

    @Override
    public String onFragmentInteractionAddress() {
        return personID;

    }

    @Override
    protected void onPause() {
        super.onPause();
        personIDEdit=null;
        addressIDEdit=null;
    }
}
