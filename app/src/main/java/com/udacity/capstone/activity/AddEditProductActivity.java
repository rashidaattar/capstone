package com.udacity.capstone.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.udacity.capstone.R;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.ProductTable;
import com.udacity.capstone.util.Constants;
import com.udacity.capstone.util.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditProductActivity extends AppCompatActivity {


    private  String selectedImagePath;
    private boolean isStoragePermission = false;
    private static final int SELECT_PICTURE = 0;
    private static final int SELECT_CAMERA = 5;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 3;
    private static final int OPEN_CAMERA = 4;
    private boolean isCameraPermission = false;
    private Uri camera_uri;

    private static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private String prodID = "";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.txt_prodname)
    EditText txt_prodname;

    @BindView(R.id.txt_prodcode)
    EditText txt_prodcode;

    @BindView(R.id.txt_prodesc)
    EditText txt_prodesc;

    @BindView(R.id.txt_quantity)
    EditText txt_quantity;

    @BindView(R.id.txt_minquantity)
    EditText txt_minquantity;

    @BindView(R.id.txt_dimension)
    EditText txt_dimension;

    @BindView(R.id.image_product)
    ImageView img_product;

    @BindView(R.id.txt_metric)
    EditText txt_metric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ADD PRODUCT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent()!=null){
            if(getIntent().hasExtra(Constants.EDIT_PRODUCTS)){
                prodID = getIntent().getStringExtra(Constants.EDIT_PRODUCTS);
            }
            if(getIntent().hasExtra(Constants.EDIT_PRODUCT_BOOLEAN)){
                populatData();
            }
        }
    }

    private void populatData() {
        if(prodID!=null && prodID.length()>0){
            Cursor cursor = getContentResolver().query(InventoryProvider.Products.PRODUCTS_URI,new String[]{ProductTable.PRODUCT_DESCRIPTION,ProductTable.PRODUCT_IMG,ProductTable.PRODUCT_NAME,
            ProductTable.MINIMUM_QUANTITY,ProductTable.PRODUCT_DIMENSION,ProductTable.PRODUCT_CODE,ProductTable.PRODUCT_METRIC,ProductTable.PRODUCT_QUANTITY}
            ,ProductTable._ID+" = "+prodID,null,null);
            if(cursor!=null && cursor.getCount()==1){
                updateUI(cursor);
            }
        }
    }

    private void updateUI(Cursor cursor) {
        cursor.moveToFirst();
        txt_prodname.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME)));
        txt_prodesc.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_DESCRIPTION)));
        txt_quantity.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_QUANTITY)));
        txt_minquantity.setText(cursor.getString(cursor.getColumnIndex(ProductTable.MINIMUM_QUANTITY)));
        txt_dimension.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_DIMENSION)));
        txt_metric.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_METRIC)));
        txt_prodcode.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_CODE)));
        img_product.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_IMG))));
        img_product.setVisibility(View.VISIBLE);

    }


    public void imageFromGallery(){
        getPermissionToGallery(READ_EXTERNAL_STORAGE_REQUEST);
    }

    public void imageFromCamera(){
        getPermissionToGallery(CAMERA_REQUEST);
    }



    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToGallery(int i) {

        switch (i){
            case READ_EXTERNAL_STORAGE_REQUEST:
                if (ContextCompat.checkSelfPermission(this, permissions[0])
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{permissions[0]}, READ_EXTERNAL_STORAGE_REQUEST);
                    //isStoragePermission = false;
                }
                else{
                   // isStoragePermission = true;
                    getImageIntent();
                }
                break;
            case CAMERA_REQUEST:
                if (ContextCompat.checkSelfPermission(this, permissions[1])
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, permissions[2])
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{permissions[1],permissions[2]},CAMERA_REQUEST);
                }
                else{
                    //isCameraPermission = true;
                    getCameraIntent();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                isStoragePermission=true;
                getImageIntent();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                isStoragePermission=false;
            }
        }
        else if(requestCode == CAMERA_REQUEST){
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
                isCameraPermission=true;
               // getImageIntent();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
                isCameraPermission=false;
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = Utility.getPathforImage(selectedImageUri,this);
                //showImage();

                Log.v("IMAGE PATH====>>>> ",selectedImagePath);
            }
            else if(requestCode == SELECT_CAMERA) {
                selectedImagePath = camera_uri.getPath();
            }
            showImage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_from_gallery:
                imageFromGallery();
                return true;

            case R.id.add_from_camera:
                imageFromCamera();
                return true;

            default:
                return false;
        }

    }

    public void getImageIntent(){
        /*Intent intent = new Intent();
            intent.setType("image");
            intent.setAction(Intent.ACTION_GET_CONTENT);*/
        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    private void getCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_uri = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, camera_uri);
        startActivityForResult(intent, SELECT_CAMERA);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    private void showImage() {
        File imgFile = new File(selectedImagePath);
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img_product.setVisibility(View.VISIBLE);
            img_product.setImageBitmap(myBitmap);
        }
    }

    @OnClick(R.id.save_product)
    public void saveProduct(View view){

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductTable.PRODUCT_NAME,txt_prodname.getText().toString());
        contentValues.put(ProductTable.PRODUCT_DESCRIPTION,txt_prodesc.getText().toString());
        contentValues.put(ProductTable.PRODUCT_CODE,txt_prodcode.getText().toString());
        contentValues.put(ProductTable.PRODUCT_DIMENSION,txt_dimension.getText().toString());
        contentValues.put(ProductTable.PRODUCT_QUANTITY,Float.valueOf(txt_quantity.getText().toString()));
        contentValues.put(ProductTable.MINIMUM_QUANTITY,Float.valueOf(txt_minquantity.getText().toString()));
        contentValues.put(ProductTable.PRODUCT_METRIC,txt_metric.getText().toString());
        if(selectedImagePath!=null && selectedImagePath.length()>0)
        contentValues.put(ProductTable.PRODUCT_IMG,selectedImagePath);
        if(getIntent().hasExtra(Constants.EDIT_PRODUCT_BOOLEAN)){
            int inserted_rows =getContentResolver().update(InventoryProvider.Products.PRODUCTS_URI,contentValues,ProductTable._ID+"="+prodID,null);
            Log.d("customerinsert","Updated customers : "+inserted_rows);
        }
        else{
            getContentResolver().insert(InventoryProvider.Products.PRODUCTS_URI,contentValues);
        }

    }
}
