package com.udacity.capstone.activity;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryDatabase;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.ProductTable;
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsDetailActivity extends AppCompatActivity {

    private String prodID;
    private Cursor mCursor;

    @BindView(R.id.main_backdrop)
    ImageView prod_img;

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_product)
    Toolbar prod_toolbar;

    @BindView(R.id.prod_desc)
    TextView prod_desc;

    @BindView(R.id.prod_code)
    TextView prod_code;

    @BindView(R.id.prod_metric)
    TextView prod_metric;

    @BindView(R.id.prod_dimension)
    TextView prod_dimension;

    @BindView(R.id.prod_min_quantity)
    TextView prod_minquant;

    @BindView(R.id.prod_quantity)
    TextView quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if(getIntent().hasExtra(Constants.VIEW_DETAIL)){
            prodID = getIntent().getStringExtra(Constants.VIEW_DETAIL);
        }
        if(prodID!=null){
            mCursor = getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,null,
                    InventoryDatabase.PRODUCTS+"."+ ProductTable._ID+"="+Integer.parseInt(prodID),null,null);
            if(mCursor!=null && mCursor.getCount()==1){
                updateUI();
            }
        }
    }

    private void updateUI() {
        mCursor.moveToFirst();
        if(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_IMG))!=null){
            prod_img.setImageBitmap(BitmapFactory.decodeFile(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_IMG))));
            getSupportActionBar().setTitle(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_NAME)));
            prod_toolbar.setVisibility(View.GONE);
        }
        else{
            prod_img.setVisibility(View.GONE);
            getSupportActionBar().setTitle("PRODUCT");
            prod_toolbar.setTitle(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_NAME)));
        }
        prod_code.setText(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_CODE)));
        prod_desc.setText(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_DESCRIPTION)));
        prod_dimension.setText(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_DIMENSION)));
        prod_metric.setText(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_METRIC)));
        quantity.setText(mCursor.getString(mCursor.getColumnIndex(ProductTable.PRODUCT_QUANTITY)));
        prod_minquant.setText(mCursor.getString(mCursor.getColumnIndex(ProductTable.MINIMUM_QUANTITY)));
        mCursor.close();
    }
}
