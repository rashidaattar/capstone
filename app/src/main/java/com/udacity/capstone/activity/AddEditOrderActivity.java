package com.udacity.capstone.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryDatabase;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.Order_ProductTable;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.database.ProductTable;
import com.udacity.capstone.util.Constants;
import com.udacity.capstone.util.OrderProductDAO;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditOrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.txt_orderno)
    EditText order_no;

    @BindView(R.id.txt_orderdate)
    EditText order_date_edittext;

    @BindView(R.id.txt_deliverydate)
    EditText delivery_date_edittext;

    @BindView(R.id.customer_auto_complete)
    AutoCompleteTextView customer_auto_complete;

    @BindView(R.id.txt_amount)
    EditText amount;

    @BindView(R.id.txt_deliveryaddressline1)
    EditText address_line1;

    @BindView(R.id.txt_deliveryaddressline2)
    EditText address_line2;

    @BindView(R.id.txt_city)
    EditText city;

    @BindView(R.id.txt_state)
    EditText state;

    @BindView(R.id.txt_pincode)
    EditText pincode;

    @BindView(R.id.product_info)
    TextView product_info;

    @BindView(R.id.status_spinner)
    AppCompatSpinner spinner;

    @BindView(R.id.cordinator_main)
    CoordinatorLayout main_layout;

    Calendar myCalendar = Calendar.getInstance();

    Context mContext;
    boolean isEdit = false;

    //data fields
    private String status;
    private int order_id;
    private int address_id;
    private int order_product_id;
    HashMap<String,String> customer_map = new HashMap<>();
    HashMap<String,String> product_quantity_map = new HashMap<>();
    private String[] status_array ;
    private boolean save_order_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);
        ButterKnife.bind(this);
        mContext = this;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.order_label));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        order_date_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DatePickerDialog(mContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        delivery_date_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DatePickerDialog(mContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        initCustomerAutoComplete();
        initSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = status_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(getIntent().hasExtra(Constants.EDIT_ORDER_BOOLEAN)){
            if(getIntent().getBooleanExtra(Constants.EDIT_ORDER_BOOLEAN,true)){
                isEdit = true;
                order_id = getIntent().getIntExtra(Constants.ORDER_ID_EXTRA,0);
                Cursor c =getContentResolver().query(InventoryProvider.OrderProduct.ORDERS_PRODUCT_PERSON_JOIN,null, InventoryDatabase.ORDER_PRODUCT+
                        "."+Order_ProductTable.ORDER_ID+" = "+order_id,null,null);
                assert c != null;
                if(c.getCount()>0){
                    while(c.moveToNext()){
                        populateData(c);
                    }
                }
                populateProducts(getIntent().<OrderProductDAO>getParcelableArrayListExtra(Constants.PRODUCT_NAME_EXTRA));
            }
        }


    }

    private void initSpinner() {
        status_array = getResources().getStringArray(R.array.status_array);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,status_array );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }

    private void updateLabel() {

        String myFormat = getString(R.string.date_format);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(order_date_edittext.getText().toString().length()==0)
        order_date_edittext.setText(sdf.format(myCalendar.getTime()));
        delivery_date_edittext.setText(sdf.format(myCalendar.getTime()));

    }

    @OnClick(R.id.add_product)
    public void addProduct(){
        new ProductAutoCompleteAsync().execute();

    }

    public void initCustomerAutoComplete(){

        Cursor cursor = mContext.getContentResolver().query(InventoryProvider.Persons.PERSONS_URI,new String[]{PersonTable.PERSON_NAME,PersonTable._ID}, PersonTable.PERSON_TYPE+" LIKE '"+PersonTable.CUSTOMER_PERSON+"'",null,null);

        if(cursor.getCount()>0){
            // productarray = new String[cursor.getCount()];
            while(cursor.moveToNext()){
                customer_map.put(cursor.getString(cursor.getColumnIndex(PersonTable.PERSON_NAME)),cursor.getString(cursor.getColumnIndex(PersonTable._ID)));
                //  productarray[i] = cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME));
            }
        }
        if(customer_map.size()>0){
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_dropdown_item_1line,
                    customer_map.keySet().toArray(new String[cursor.getCount()]));
            customer_auto_complete.setAdapter(stringArrayAdapter);
            customer_auto_complete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        customer_auto_complete.showDropDown();
                    }
                }
            });
        }
        else{
            save_order_flag = false;
            customer_auto_complete.setEnabled(false);
            customer_auto_complete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        Toast.makeText(mContext,getString(R.string.no_customer_toast),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    @OnClick(R.id.save_order)
    public void saveOrder(View view){
        if(save_order_flag){
            insertAddress();
            insertOrder();
            insertorderProduct();
        }
        else{
            Toast.makeText(mContext,getString(R.string.no_order_toast),Toast.LENGTH_SHORT).show();
        }

    }

    public void insertOrder(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(OrdersTable.ORDER_NUMBER,order_no.getText().toString());
        contentValues.put(OrdersTable.ORDER_DATE,order_date_edittext.getText().toString());
        contentValues.put(OrdersTable.DELIVERY_DATE,delivery_date_edittext.getText().toString());
        contentValues.put(OrdersTable.ORDER_AMOUNT,amount.getText().toString());
        contentValues.put(OrdersTable.ORDER_STATUS,status);
        contentValues.put(OrdersTable.ADDRESS_ID,address_id);
        if(isEdit){
            getContentResolver().update(InventoryProvider.Orders.ORDERS_URI,contentValues,OrdersTable._ID+"="+order_id,null);
        }
        else{
            Uri uri = getContentResolver().insert(InventoryProvider.Orders.ORDERS_URI,contentValues);
            String id[] = uri != null ? uri.getPath().toString().split("/") : new String[0];
            order_id =Integer.parseInt(id[2]);
        }

    }

    public void insertAddress(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AddressTable.ADDRESS_LINE1,address_line1.getText().toString());
        contentValues.put(AddressTable.ADDRESS_LINE2,address_line2.getText().toString());
        contentValues.put(AddressTable.CITY,city.getText().toString());
        contentValues.put(AddressTable.STATE,state.getText().toString());
        contentValues.put(AddressTable.PINCODE,pincode.getText().toString());
        contentValues.put(AddressTable.ADDRESS_TYPE,AddressTable.ADDRESS_SHIPPING);
        contentValues.put(AddressTable.PERSON_ID,Integer.parseInt(customer_map.get(customer_auto_complete.getText().toString())));
        if(isEdit){
            getContentResolver().update(InventoryProvider.Addreses.ADDRESSES_URI,contentValues,AddressTable._ID+"="+address_id,null);
        }
        else{
            Uri uri = getContentResolver().insert(InventoryProvider.Addreses.ADDRESSES_URI,contentValues);
            String id[] = uri.getPath().toString().split("/");
            address_id =Integer.parseInt(id[2]);
        }

    }

    public void insertorderProduct(){
        Set<HashMap.Entry<String, String>> entrySet = product_quantity_map.entrySet();
        for (Map.Entry entry : entrySet) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Order_ProductTable.ORDER_ID,order_id);
            contentValues.put(Order_ProductTable.PRODUCT_NAME,(String)entry.getKey());
            contentValues.put(Order_ProductTable.PRODUCT_QUANTITY,Float.valueOf((String)entry.getValue()));
            if(isEdit){
                getContentResolver().update(InventoryProvider.OrderProduct.ORDER_PRODUCT_URI,contentValues,Order_ProductTable._ID+"="+order_product_id,null);
            }
            else{
                getContentResolver().insert(InventoryProvider.OrderProduct.ORDER_PRODUCT_URI,contentValues);
            }
        }
        Snackbar snackbar = Snackbar
                .make(main_layout, getResources().getString(R.string.add_toast), Snackbar.LENGTH_LONG);
        snackbar.show();
        finish();
    }

    private void populateProducts(ArrayList<OrderProductDAO> parcelableArrayListExtra) {
        product_info.setVisibility(View.VISIBLE);
        for(OrderProductDAO orderProductDAO:parcelableArrayListExtra){
            product_info.setText(orderProductDAO.getProd_name() + "\n"+getString(R.string.quantity)+" :"+orderProductDAO.getProd_quantity()+"\n");
            product_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ProductAutoCompleteAsync().execute();
                }
            });
        }
    }

    private void populateData(Cursor c) {
        order_no.setText(c.getString(c.getColumnIndex(OrdersTable.ORDER_NUMBER)));
        order_date_edittext.setText(c.getString(c.getColumnIndex(OrdersTable.ORDER_DATE)));
        delivery_date_edittext.setText(c.getString(c.getColumnIndex(OrdersTable.DELIVERY_DATE)));
        amount.setText(c.getString(c.getColumnIndex(OrdersTable.ORDER_AMOUNT)));
        spinner.setPrompt(c.getString(c.getColumnIndex(OrdersTable.ORDER_STATUS)));
        address_line1.setText(c.getString(c.getColumnIndex(AddressTable.ADDRESS_LINE1)));
        address_line2.setText(c.getString(c.getColumnIndex(AddressTable.ADDRESS_LINE2)));
        city.setText(c.getString(c.getColumnIndex(AddressTable.CITY)));
        state.setText(c.getString(c.getColumnIndex(AddressTable.STATE)));
        pincode.setText(c.getString(c.getColumnIndex(AddressTable.PINCODE)));
        customer_auto_complete.setText(c.getString(c.getColumnIndex(PersonTable.PERSON_NAME)));
        order_id = c.getInt(c.getColumnIndex(Order_ProductTable.ORDER_ID));
        address_id = c.getInt(c.getColumnIndex(AddressTable._ID));
        order_product_id = c.getInt(c.getColumnIndex(Order_ProductTable._ID));
    }


    public class ProductAutoCompleteAsync extends AsyncTask<Void,Cursor,Cursor>{

         AutoCompleteTextView productautoAutoCompleteTextView;
        EditText quantity;
        AlertDialog.Builder builder;
        View alert_view;
        String product_quantity;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            alert_view = inflater.inflate(R.layout.addproduct_child, null);
            productautoAutoCompleteTextView = (AutoCompleteTextView) alert_view.findViewById(R.id.product_autocomplete);
            quantity= (EditText) alert_view.findViewById(R.id.product_quantity);

        }

        @Override
        protected Cursor doInBackground(Void... params) {
            return mContext.getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,new String[]{ProductTable.PRODUCT_NAME,ProductTable._ID,ProductTable.PRODUCT_QUANTITY},null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            final HashMap<String,String> productMap = new HashMap<>();
            if(cursor!=null && cursor.getCount()>0){
                while(cursor.moveToNext()){
                    productMap.put(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME)),cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_QUANTITY)));
                }
                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_dropdown_item_1line,productMap.keySet().toArray(new String[cursor.getCount()]));
                productautoAutoCompleteTextView.setAdapter(stringArrayAdapter);
                productautoAutoCompleteTextView.setThreshold(1);
                productautoAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            productautoAutoCompleteTextView.showDropDown();
                        }
                    }
                });
                builder.setView(alert_view);
                builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String available_quantity = productMap.get(productautoAutoCompleteTextView.getText().toString());
                        product_quantity = quantity.getText().toString();
                        if(Float.valueOf(available_quantity)<=Float.valueOf(product_quantity))
                        {
                            Toast.makeText(mContext,getString(R.string.quantity_less),Toast.LENGTH_SHORT).show();
                        }
                        if(product_info.getVisibility()!=View.VISIBLE){
                            product_info.setVisibility(View.VISIBLE);
                        }
                        if(product_info.getText().toString()==null){
                            product_info.setText(productautoAutoCompleteTextView.getText().toString()+"\n"+getString(R.string.quantity)+" :"+quantity.getText().toString());
                        }
                        else{
                            String info=product_info.getText().toString();
                            product_info.setText(info + "\n"+productautoAutoCompleteTextView.getText().toString()+"\n"+getString(R.string.quantity)+" :"+quantity.getText().toString());
                        }
                        product_quantity_map.put(productautoAutoCompleteTextView.getText().toString(),product_quantity);
                    }

                });
                builder.show();
            }
            else{
                save_order_flag = false;
                Toast.makeText(mContext,getString(R.string.no_product_toast),Toast.LENGTH_SHORT).show();
            }

        }
    }
}
