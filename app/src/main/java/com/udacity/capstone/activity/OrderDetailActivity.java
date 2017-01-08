package com.udacity.capstone.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryDatabase;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.Order_ProductTable;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.util.Constants;
import com.udacity.capstone.util.OrderProductDAO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends AppCompatActivity {

    int order_id;
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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_customer)
    Toolbar toolbar_customer;
    @BindView(R.id.toolbar_address)
    Toolbar toolbar_adress;
    @BindView(R.id.toolbar_order)
    Toolbar toolbar_order;
    @BindView(R.id.toolbar_product)
    Toolbar toolbar_product;

    @BindView(R.id.order_number)
    TextView order_number;
    @BindView(R.id.order_date)
    TextView order_date;
    @BindView(R.id.delivery_date)
    TextView delivery_date;
    @BindView(R.id.order_amount)
    TextView order_amount;

    @BindView(R.id.customer_name)
    TextView customer_name;
    @BindView(R.id.customer_company)
    TextView cusomer_company;

    @BindView(R.id.product_main)
    LinearLayout product_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.order_label));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_customer.setTitle(getString(R.string.customer_label));
        toolbar_adress.setTitle(getString(R.string.address_info));
        toolbar_product.setTitle(getString(R.string.product_info));
        toolbar_order.setTitle(getString(R.string.order_info));
        order_id = getIntent().getIntExtra(Constants.ORDER_ID_EXTRA,0);
        Cursor c =getContentResolver().query(InventoryProvider.OrderProduct.ORDERS_PRODUCT_PERSON_JOIN,null, InventoryDatabase.ORDER_PRODUCT+
                "."+Order_ProductTable.ORDER_ID+" = "+order_id,null,null);
        if(c.getCount()>0){
            while(c.moveToNext()){
                populateData(c);
            }
        }
        populateProducts(getIntent().<OrderProductDAO>getParcelableArrayListExtra(Constants.PRODUCT_NAME_EXTRA));
    }

    private void populateProducts(ArrayList<OrderProductDAO> stringArrayListExtra) {
        for(OrderProductDAO orderProductDAO:stringArrayListExtra){
            LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setTextSize(getResources().getDimension(R.dimen.textSizeVeryVeryVerySmall));
            tv.setText(orderProductDAO.getProd_name() + "\n"+"Quantity :"+orderProductDAO.getProd_quantity()+"\n");
            product_main.addView(tv);

        }
    }

    private void populateData(Cursor c) {
        order_number.setText(getString(R.string.order_number)+" : "+c.getString(c.getColumnIndex(OrdersTable.ORDER_NUMBER)));
        order_amount.setText(getString(R.string.amount)+" : "+c.getString(c.getColumnIndex(OrdersTable.ORDER_AMOUNT)));
        order_date.setText(getString(R.string.order_date)+" : "+c.getString(c.getColumnIndex(OrdersTable.ORDER_DATE)));
        delivery_date.setText(getString(R.string.delivery_date)+" : "+c.getString(c.getColumnIndex(OrdersTable.DELIVERY_DATE)));

        customer_name.setText(getString(R.string.customer_name)+" : "+c.getString(c.getColumnIndex(PersonTable.PERSON_NAME)));
        cusomer_company.setText(getString(R.string.companyname_label)+" : "+c.getString(c.getColumnIndex(PersonTable.COMPANY_NAME)));

        address_line1.setText(getString(R.string.address1_label)+" : "+c.getString(c.getColumnIndex(AddressTable.ADDRESS_LINE1)));
        address_line2.setText(getString(R.string.address2_label)+" : "+c.getString(c.getColumnIndex(AddressTable.ADDRESS_LINE2)));
        state.setText(getString(R.string.state_label)+" : "+c.getString(c.getColumnIndex(AddressTable.STATE)));
        city.setText(getString(R.string.city_label)+" : "+c.getString(c.getColumnIndex(AddressTable.CITY)));
        pincode.setText(getString(R.string.pincode_label)+" : "+c.getString(c.getColumnIndex(AddressTable.PINCODE)));
    }
}
