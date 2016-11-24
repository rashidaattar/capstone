package com.udacity.capstone.activity;


import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.udacity.capstone.R;
import com.udacity.capstone.adapter.CustomersCursorAdapter;
import com.udacity.capstone.database.InventoryDatabase;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.util.Constants;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;
    InventoryDatabase inventoryDatabase;


    @BindView(R.id.customer_fab)
    FloatingActionButton customer_fab;

    @BindView(R.id.customers_list)
    RecyclerView customersList;

    private static final int CURSOR_LOADER_ID = 0;

    private Cursor mCursor;

    private CustomersCursorAdapter mCustomersCursorAdapter;

    private boolean isCustomer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        ButterKnife.bind(this);
        if(getIntent().hasExtra(Constants.PERSON_TYPE_LABEL)){
            if(getIntent().getStringExtra(Constants.PERSON_TYPE_LABEL).equals(Constants.CUSTOMER_TYPE)){
                isCustomer = true;
            }
            else{
                isCustomer = false;
            }
        }
        setSupportActionBar(mtoolbar);
        if(isCustomer)
            getSupportActionBar().setTitle("Customers");
        else
            getSupportActionBar().setTitle("Vendors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        mCustomersCursorAdapter = new CustomersCursorAdapter(this,mCursor,isCustomer);
        customersList.setLayoutManager(new GridLayoutManager(this,2));
        customersList.setAdapter(mCustomersCursorAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(isCustomer)
            return new CursorLoader(this, InventoryProvider.Persons.PERSONS_JOIN_URI, null,PersonTable.PERSON_TYPE+" LIKE '"+PersonTable.CUSTOMER_PERSON+"'",null,null);
        else
            return new CursorLoader(this, InventoryProvider.Persons.PERSONS_JOIN_URI, null,PersonTable.PERSON_TYPE+" LIKE '"+PersonTable.VENDOR_PERSON+"'",null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCustomersCursorAdapter.swapCursor(data);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCustomersCursorAdapter.swapCursor(null);

    }

    @OnClick(R.id.customer_fab)
    public void addCustomer(){

        Intent intent=new Intent(this,AddEditCustomerActivity.class);
        intent.putExtra(Constants.PERSON_TYPE_LABEL,isCustomer);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID,null,this);
    }
}
