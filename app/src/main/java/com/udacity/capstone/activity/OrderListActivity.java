package com.udacity.capstone.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.udacity.capstone.R;
import com.udacity.capstone.adapter.OrdersCursorAdapter;
import com.udacity.capstone.database.InventoryProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends AppCompatActivity  implements  LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.orders_list)
    RecyclerView orders_list;

    private static final int CURSOR_LOADER_ID = 0;

    private Cursor mCursor;

    private OrdersCursorAdapter mOrdersCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //noinspection deprecation
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mOrdersCursorAdapter = new OrdersCursorAdapter(this,mCursor);
        orders_list.setLayoutManager(new LinearLayoutManager(this));
        orders_list.setAdapter(mOrdersCursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @OnClick(R.id.addorder_fab)
    public void addOrder(){
        Intent intent = new Intent(this, AddEditOrderActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, InventoryProvider.Orders.ORDERS_PRODUCT_JOIN,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mOrdersCursorAdapter.swapCursor(data);
        mCursor = data;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mOrdersCursorAdapter.swapCursor(null);

    }
}
