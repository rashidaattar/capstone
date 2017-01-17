package com.udacity.capstone.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryDatabase;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.util.Constants;
import com.udacity.capstone.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDetailActivity extends AppCompatActivity {

    private String personID;
    private Cursor mCursor;

    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.address_line1)
    TextView address_line1;
    @BindView(R.id.address_line2)
    TextView address_line2;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.pincode)
    TextView pincode;
    @BindView(R.id.contact_number)
    TextView company_number;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_customer)
    Toolbar toolbar_customer;
    @BindView(R.id.toolbar_address)
    Toolbar toolbar_adress;

    private Context mContext;
    private String locationString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mContext = this;
        toolbar_adress.setTitle(getString(R.string.address_info));
        getSupportActionBar().setTitle(getString(R.string.details_label));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent()!=null){
            personID = getIntent().getStringExtra(Constants.VIEW_DETAIL);
            toolbar_customer.setTitle(getIntent().getStringExtra(Constants.VIEW_CUSTOMER_DETAIL_NAME));
        }
        if(personID!=null){
            mCursor = getContentResolver().query(InventoryProvider.Persons.PERSONS_JOIN_URI,null, InventoryDatabase.PERSONS+"."+PersonTable._ID+"="+Integer.parseInt(personID)+" and "
                    +InventoryDatabase.ADRESSES+"."+AddressTable.ADDRESS_TYPE+" LIKE '"+AddressTable.ADDRESS_BILLING+"'",null,null);
            if(mCursor!=null){
                updateUI();
            }
        }
        toolbar_customer.inflateMenu(R.menu.call_email_contextmenu);
        toolbar_customer.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.call_button:
                        Utility.callPerson(mContext,mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO)));
                        return true;

                    case R.id.mail_button:
                        Utility.emailPerson(mContext,mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL)));
                        return true;

                    default:
                        return false;
                }
            }
        });

        toolbar_adress.inflateMenu(R.menu.maps_menu);
        toolbar_adress.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.maps_button){
                    //maps logic here
                    Intent intent=new Intent(mContext,MapsActivity.class);
                    intent.putExtra(Constants.MAP_EXTRA,locationString);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void updateUI() {

            if(mCursor.moveToNext()){
                //   name.setText(mCursor.getString(mCursor.getColumnIndex(PersonTable.PERSON_NAME)));
                if(mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO))!=null)
                    mobile.setText(getString(R.string.mobile_label)+" : "+mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO)));
                if(mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL))!=null)
                    email.setText(getString(R.string.email_label)+" : "+mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL)));
                if(mCursor.getString(mCursor.getColumnIndex(PersonTable.COMPANY_NAME))!=null)
                    company_name.setText(getString(R.string.companyname_label)+" : "+mCursor.getString(mCursor.getColumnIndex(PersonTable.COMPANY_NAME)));
                if(mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE1))!=null)
                    address_line1.setText(getString(R.string.address1_label)+" : "+mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE1)));
                if(mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE2))!=null)
                    address_line2.setText(getString(R.string.address2_label)+" : "+mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE2)));
                if(mCursor.getString(mCursor.getColumnIndex(AddressTable.CITY))!=null)
                    city.setText(getString(R.string.city_label)+" : "+mCursor.getString(mCursor.getColumnIndex(AddressTable.CITY)));
                if(mCursor.getString(mCursor.getColumnIndex(AddressTable.STATE))!=null)
                    state.setText(getString(R.string.state_label)+" : "+mCursor.getString(mCursor.getColumnIndex(AddressTable.STATE)));
                if(mCursor.getString(mCursor.getColumnIndex(AddressTable.PINCODE))!=null)
                    pincode.setText(getString(R.string.pincode_label)+" : "+mCursor.getString(mCursor.getColumnIndex(AddressTable.PINCODE)));
                if(mCursor.getString(mCursor.getColumnIndex(AddressTable.CONTACT_NO))!=null)
                    company_number.setText(getString(R.string.contact_label)+" : "+mCursor.getString(mCursor.getColumnIndex(AddressTable.CONTACT_NO)));
                // locationString = mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE1))+" , "+ mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE2));
                locationString = mCursor.getString(mCursor.getColumnIndex(AddressTable.PINCODE));
            }


    }
}
