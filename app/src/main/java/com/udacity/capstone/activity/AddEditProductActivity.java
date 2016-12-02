package com.udacity.capstone.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.udacity.capstone.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditProductActivity extends AppCompatActivity {


    private String selectedImagePath;
    private boolean isStoragePermission = false;
    private static final int SELECT_PICTURE = 0;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 3;
    private static final int OPEN_CAMERA = 4;
    private boolean isCameraPermission = false;

    private static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ADD PRODUCT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void imageFromGallery(){
        if(isStoragePermission){
           getImageIntent();
        }
        else{
            getPermissionToGallery(READ_EXTERNAL_STORAGE_REQUEST);

        }
    }

    public void imageFromCamera(){
        if(!isCameraPermission){
            getPermissionToGallery(CAMERA_REQUEST);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToGallery(int i) {

        switch (i){
            case READ_EXTERNAL_STORAGE_REQUEST:
                if (ContextCompat.checkSelfPermission(this, permissions[0])
                        != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{permissions[0]}, READ_EXTERNAL_STORAGE_REQUEST);
                break;
            case CAMERA_REQUEST:
                if (ContextCompat.checkSelfPermission(this, permissions[1])
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, permissions[2])
                        != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{permissions[1],permissions[2]},CAMERA_REQUEST);
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
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
}
