package com.udacity.capstone.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.capstone.R;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.Order_ProductTable;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.database.ProductTable;


import java.text.SimpleDateFormat;
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

    Calendar myCalendar = Calendar.getInstance();

    Context mContext;

    //data fields
    private String orderNo;
    private String order_date;
    private String status;
    private int order_id;
    private int address_id;
    HashMap<String,String> customer_map = new HashMap<>();
    HashMap<String,String> product_quantity_map = new HashMap<>();
    private String[] status_array ;

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


    }

    private void initSpinner() {
        status_array = getResources().getStringArray(R.array.status_array);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,status_array );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
    }

    private void updateLabel() {

        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(order_date_edittext.getText().toString().length()==0)
        order_date_edittext.setText(sdf.format(myCalendar.getTime()));
        delivery_date_edittext.setText(sdf.format(myCalendar.getTime()));

    }

    @OnClick(R.id.add_product)
    public void addProduct(){
       //display a dialog with an autocomplete text view, on selecting a product, add a textview to disply the product
       // callDialog();
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
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_dropdown_item_1line,
                customer_map.keySet().toArray(new String[cursor.getCount()]));
        customer_auto_complete.setAdapter(stringArrayAdapter);
    }

    @OnClick(R.id.save_order)
    public void saveOrder(View view){
        insertAddress();
        insertOrder();
        insertorderProduct();
    }

    public void insertOrder(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(OrdersTable.ORDER_NUMBER,order_no.getText().toString());
        contentValues.put(OrdersTable.ORDER_DATE,order_date_edittext.getText().toString());
        contentValues.put(OrdersTable.DELIVERY_DATE,delivery_date_edittext.getText().toString());
        contentValues.put(OrdersTable.ORDER_AMOUNT,amount.getText().toString());
        contentValues.put(OrdersTable.ORDER_STATUS,status);
       // contentValues.put(OrdersTable.ADDRESS_ID,address_id);
        contentValues.put(OrdersTable.ADDRESS_ID,address_id);
        Uri uri = getContentResolver().insert(InventoryProvider.Orders.ORDERS_URI,contentValues);
        String id[] = uri.getPath().toString().split("/");
        order_id =Integer.parseInt(id[2]);
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
        Uri uri = getContentResolver().insert(InventoryProvider.Addreses.ADDRESSES_URI,contentValues);
        String id[] = uri.getPath().toString().split("/");
        address_id =Integer.parseInt(id[2]);
    }

    public void insertorderProduct(){
        Set<HashMap.Entry<String, String>> entrySet = product_quantity_map.entrySet();
        for (Map.Entry entry : entrySet) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
            ContentValues contentValues = new ContentValues();
            contentValues.put(Order_ProductTable.ORDER_ID,order_id);
            contentValues.put(Order_ProductTable.PRODUCT_NAME,(String)entry.getKey());
            contentValues.put(Order_ProductTable.PRODUCT_QUANTITY,Float.valueOf((String)entry.getValue()));
            getContentResolver().insert(InventoryProvider.OrderProduct.ORDER_PRODUCT_URI,contentValues);

        }


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
            Cursor cursor = mContext.getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,new String[]{ProductTable.PRODUCT_NAME,ProductTable._ID,ProductTable.PRODUCT_QUANTITY},null,null,null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            final HashMap<String,String> productMap = new HashMap<>();
            if(cursor.getCount()>0){
                while(cursor.moveToNext()){
                    productMap.put(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME)),cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_QUANTITY)));
                }
            }
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_dropdown_item_1line,productMap.keySet().toArray(new String[cursor.getCount()]));
            productautoAutoCompleteTextView.setAdapter(stringArrayAdapter);
            productautoAutoCompleteTextView.setThreshold(2);
            builder.setView(alert_view);
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String available_quantity = productMap.get(productautoAutoCompleteTextView.getText().toString());
                    product_quantity = quantity.getText().toString();
                    if(Integer.parseInt(available_quantity)<=Integer.parseInt(product_quantity))
                    {
                        Toast.makeText(mContext,"Available quantity less than required quantity. Kindly update the inventory!",Toast.LENGTH_SHORT).show();
                    }
                    if(product_info.getVisibility()!=View.VISIBLE){
                        product_info.setVisibility(View.VISIBLE);
                    }
                    if(product_info.getText().toString()==null){
                        product_info.setText(productautoAutoCompleteTextView.getText().toString()+"\nQuantity :"+quantity.getText().toString());
                    }
                    else{
                        String info=product_info.getText().toString();
                        product_info.setText(info + "\n"+productautoAutoCompleteTextView.getText().toString()+"\nQuantity :"+quantity.getText().toString());
                    }
                    product_quantity_map.put(productautoAutoCompleteTextView.getText().toString(),product_quantity);
                }

            });
            builder.show();

        }
    }
}
