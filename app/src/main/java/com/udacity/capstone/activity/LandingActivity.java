package com.udacity.capstone.activity;

import android.content.ContentValues;
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
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("CHECK");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

    }

    @OnClick(R.id.prod_button)
    public void prodClick(){

        Cursor c=getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,new String[]{"_id"},null,null,null);
        Log.d("here", "checking size: " + c.getCount());

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

}
