package com.udacity.capstone.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.capstone.R;
import com.udacity.capstone.adapter.ProductsCursorAdapter;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.ProductTable;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductListActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>  {

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    @BindView(R.id.add_fab)
    FloatingActionButton add_fab;

    @BindView(R.id.products_list)
    RecyclerView productsList;

    private static final int CURSOR_LOADER_ID = 0;

    private Cursor mCursor;

    private ProductsCursorAdapter mProductsCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        mProductsCursorAdapter = new ProductsCursorAdapter(this,mCursor);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        productsList.setAdapter(mProductsCursorAdapter);

    }




    @OnClick(R.id.add_fab)
    public void addProduct(){
     /*   AddProductFragment addProductFragment=AddProductFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddProductFragment())
                .addToBackStack("products")
                .commit();*/
        Intent intent = new Intent(this, AddEditProductActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, InventoryProvider.Products.PRODUCTS_URI,
                new String[]{ProductTable._ID,ProductTable.PRODUCT_NAME,ProductTable.PRODUCT_DESCRIPTION,ProductTable.PRODUCT_IMG},null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mProductsCursorAdapter.swapCursor(data);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductsCursorAdapter.swapCursor(null);
    }
}
