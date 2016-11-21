package com.udacity.capstone.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by 836137 on 21-11-2016.
 */

public class Utility {

    public static void callPerson(Context context,String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }

    public static void emailPerson(Context context, String emailID){

        try {
            // Previous intent was changed for case when gmail is not installed
            if(emailID!=null || emailID.length()>0){
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/html");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailID});
                //startActivity(Intent.createChooser(intent, "Send Email By"));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
            else{
                Toast.makeText(context,"No email-id found",Toast.LENGTH_SHORT).show();
            }


        }catch (Exception ex){
            ex.printStackTrace();
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setMessage("Sorry! we could not find an email app. ");
            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
