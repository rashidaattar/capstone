package com.udacity.capstone.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.ProductTable;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditOrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.txt_orderno)
    EditText order_no;

    @BindView(R.id.txt_orderdate)
    EditText order_date;

    @BindView(R.id.content_add_edit_order)
     LinearLayout parent_layout;

    Calendar myCalendar = Calendar.getInstance();

    Context mContext;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);
        ButterKnife.bind(this);
        mContext = this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        order_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }
    private void updateLabel() {

        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        order_date.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.add_product)
    public void addProduct(){
       //display a dialog with an autocomplete text view, on selecting a product, add a textview to disply the product
        callDialog();
    }

    public void callDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View alert_view = inflater.inflate(R.layout.addproduct_child, null);
        final AutoCompleteTextView productautoAutoCompleteTextView = (AutoCompleteTextView) alert_view.findViewById(R.id.product_autocomplete);
        final EditText quantity = (EditText) alert_view.findViewById(R.id.product_quantity);
        Cursor cursor = mContext.getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,new String[]{ProductTable.PRODUCT_NAME},null,null,null);
        final HashMap<String,String> productMap = new HashMap<>();
        String[] productarray = new String[0];
        if(cursor.getCount()>0){
            productarray = new String[cursor.getCount()];
            int i=0;
            while(cursor.moveToNext()){
                productMap.put(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME)),cursor.getString(cursor.getColumnIndex(ProductTable._ID)));
                productarray[i] = cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME));
                i++;
            }
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_dropdown_item_1line,productMap.keySet().toArray(new String[cursor.getCount()]));
        productautoAutoCompleteTextView.setAdapter(stringArrayAdapter);
        productautoAutoCompleteTextView.setThreshold(2);
        builder.setView(alert_view);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productMap.get(productautoAutoCompleteTextView.getText().toString());
                quantity.getText().toString();
            }
        });
        builder.show();

    }
}
