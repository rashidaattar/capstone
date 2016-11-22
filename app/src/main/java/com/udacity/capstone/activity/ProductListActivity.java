package com.udacity.capstone.activity;

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
import com.udacity.capstone.fragment.AddProductFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductListActivity extends AppCompatActivity implements AddProductFragment.OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor>  {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_button :
                AddProductFragment addProductFragment=AddProductFragment.newInstance("1","2");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AddProductFragment())
                        .addToBackStack("products")
                        .commit();
                return true;

            case R.id.edit_button :
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.add_fab)
    public void addProduct(){
        AddProductFragment addProductFragment=AddProductFragment.newInstance("1","2");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddProductFragment())
                .addToBackStack("products")
                .commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, InventoryProvider.Products.PRODUCTS_URI,
                new String[]{ProductTable._ID,ProductTable.PRODUCT_NAME},null,null,null);
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
