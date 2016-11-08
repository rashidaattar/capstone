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
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.database.ProductTable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandingActivity extends AppCompatActivity {

    @BindView(R.id.prod_button)
    Button productButton;

    @BindView(R.id.order_button)
    Button orderButton;

    @BindView(R.id.toolbar_main)
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

        String i= "a";
        int k=0;
        ContentValues contentValues= new ContentValues();
        contentValues.put(ProductTable.PRODUCT_NAME,i+k++);
        getContentResolver().insert(InventoryProvider.Products.PRODUCTS_URI,contentValues);

        Cursor c=getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,new String[]{"_id"},null,null,null);
        Log.d("here", "checking size: " + c.getCount());

        Intent intent=new Intent(this,ProductListActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.order_button)
    public void orderClick(){

        int i=1;
        ContentValues contentValues= new ContentValues();
        contentValues.put(OrdersTable.ORDER_NUMBER,i++);
        getContentResolver().insert(InventoryProvider.Orders.ORDERS_URI,contentValues);

        Cursor c=getContentResolver().query(InventoryProvider.Orders.ORDERS_URI,new String[]{OrdersTable._ID},null,null,null);
        Log.d("here", "checking size: " + c.getCount());


    }
}
