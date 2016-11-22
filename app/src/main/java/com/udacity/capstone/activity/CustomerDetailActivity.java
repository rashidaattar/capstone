package com.udacity.capstone.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryDatabase;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDetailActivity extends AppCompatActivity {

    private String personID;
    private Cursor mCursor;

    @BindView(R.id.name)
    TextView name;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        toolbar_adress.setTitle("BILLING ADDRESS");
        toolbar_customer.setTitle("PERSONAL DETAILS");
        if(getIntent()!=null){
            personID = getIntent().getStringExtra(Constants.VIEW_CUSTOMER_DETAIL);
            getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.VIEW_CUSTOMER_DETAIL_NAME));
        }
        if(personID!=null){
            mCursor = getContentResolver().query(InventoryProvider.Persons.PERSONS_JOIN_URI,null, InventoryDatabase.PERSONS+"."+PersonTable._ID+"="+Integer.parseInt(personID)+" and "
                    +InventoryDatabase.ADRESSES+"."+AddressTable.ADDRESS_TYPE+" LIKE '"+AddressTable.ADDRESS_BILLING+"'",null,null);
            if(mCursor!=null){
                updateUI();
            }
        }
    }

    private void updateUI() {
        while (mCursor.moveToNext()){

            name.setText(mCursor.getString(mCursor.getColumnIndex(PersonTable.PERSON_NAME)));
            mobile.setText(mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO)));
            email.setText(mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL)));
            company_name.setText(mCursor.getString(mCursor.getColumnIndex(PersonTable.COMPANY_NAME)));

            address_line1.setText(mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE1)));
            address_line2.setText(mCursor.getString(mCursor.getColumnIndex(AddressTable.ADDRESS_LINE2)));
            city.setText(mCursor.getString(mCursor.getColumnIndex(AddressTable.CITY)));
            state.setText(mCursor.getString(mCursor.getColumnIndex(AddressTable.STATE)));
            pincode.setText(mCursor.getString(mCursor.getColumnIndex(AddressTable.PINCODE)));
            company_number.setText(mCursor.getString(mCursor.getColumnIndex(AddressTable.CONTACT_NO)));
        }
    }
}
