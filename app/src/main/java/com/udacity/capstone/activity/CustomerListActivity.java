package com.udacity.capstone.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.udacity.capstone.R;
import com.udacity.capstone.adapter.CustomersCursorAdapter;
import com.udacity.capstone.adapter.ProductsCursorAdapter;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.database.ProductTable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.toolbar_main1)
    Toolbar mtoolbar;

    @BindView(R.id.customer_fab)
    FloatingActionButton customer_fab;

    @BindView(R.id.customers_list)
    RecyclerView customersList;

    private static final int CURSOR_LOADER_ID = 0;

    private Cursor mCursor;

    private CustomersCursorAdapter mCustomersCursorAdapter;
    private ActionMode.Callback mActionModeCallback;
    private ActionMode mActionMode;
    private ActionModeCallback actionModeCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        ButterKnife.bind(this);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        mCustomersCursorAdapter = new CustomersCursorAdapter(this,mCursor);
        customersList.setLayoutManager(new LinearLayoutManager(this));
        customersList.setAdapter(mCustomersCursorAdapter);
       // registerForContextMenu(customersList);
        actionModeCallback = new ActionModeCallback();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, InventoryProvider.Persons.PERSONS_JOIN_URI,
                new String[]{PersonTable._ID,PersonTable.PERSON_NAME,PersonTable.COMPANY_NAME},null,null,null);
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
        startActivity(intent);

    }

    public void longClickListener(int adapterPosition){
        if(mActionMode==null){
            mActionModeCallback = new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.edit_delete_contextmenu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.edit_button:
                            mode.finish();
                            return true;
                        case R.id.delete_button:
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            };
            mActionMode = this.startSupportActionMode(mActionModeCallback);
        }
      /*  mActionMode=startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.edit_delete_contextmenu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });*/
    }

private class ActionModeCallback implements ActionMode.Callback{

    ActionModeCallback(){

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.edit_delete_contextmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
}
