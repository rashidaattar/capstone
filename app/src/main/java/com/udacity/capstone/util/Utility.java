package com.udacity.capstone.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.udacity.capstone.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Rashida on 21-11-2016.
 */

public class Utility {

    private static final String TEXT_HTML = "text/html";

    public static void callPerson(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(context.getString(R.string.tel)+phone));
        context.startActivity(intent);
    }

    public static void emailPerson(Context context, String emailID){

        try {
            // Previous intent was changed for case when gmail is not installed
            if(emailID!=null || emailID.length()>0){
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType(TEXT_HTML);
                intent.setData(Uri.parse(context.getString(R.string.mail_to)));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailID});
                //startActivity(Intent.createChooser(intent, "Send Email By"));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
            else{
                Toast.makeText(context,context.getString(R.string.no_emailid),Toast.LENGTH_SHORT).show();
            }


        }catch (Exception ex){
            ex.printStackTrace();
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.no_email_app));
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    public static String getPathforImage(Uri uri, Context context) {
        String wholeID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            wholeID = DocumentsContract.getDocumentId(uri);
        }

        // Split at colon, use second item in the array
        String id = null;
        if (wholeID != null) {
            id = wholeID.split(":")[1];
        }

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;

    }

}
