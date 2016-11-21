package com.udacity.capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.activity.AddEditCustomerActivity;
import com.udacity.capstone.activity.CustomerListActivity;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.util.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 836137 on 09-11-2016.
 */

public class CustomersCursorAdapter extends InventoryCursorAdapter<CustomersCursorAdapter.ViewHolder> {

    public Context mContext;
    public ActionMode mActionMode;
    public Cursor mCursor;


    public CustomersCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext= context;
        mCursor = cursor;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor, final int position) {

        viewHolder.customer_name.setText(cursor.getString(cursor.getColumnIndex(PersonTable.PERSON_NAME)).replace("_"," "));
        viewHolder.company_name.setText(cursor.getString(cursor.getColumnIndex(PersonTable.COMPANY_NAME)));
        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddEditCustomerActivity.class);

            }

        });
        viewHolder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                mCursor=getCursor(); //obtain cursor with updated data
                mCursor.moveToPosition(position); //move cursor to the current position selected
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    v.setBackgroundColor(mContext.getColor(R.color.cardview_dark_background));
                    mActionMode=v.startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater inflater = mode.getMenuInflater();
                            inflater.inflate(R.menu.edit_delete_contextmenu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_button:
                                    mode.finish();
                                    return true;
                                case R.id.delete_button:
                                    deleteCustomer();
                                    mode.finish();
                                    return true;
                                case R.id.call_button:
                                    Utility.callPerson(mContext,mCursor.getString(mCursor.getColumnIndex(PersonTable.CONTACT_NO)));
                                    mode.finish();
                                    return true;
                                case R.id.mail_button:
                                    Utility.emailPerson(mContext,mCursor.getString(mCursor.getColumnIndex(PersonTable.EMAIL)));
                                    mode.finish();
                                    return true;
                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                            if(mActionMode!=null){
                                mActionMode=null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    v.setBackgroundColor(mContext.getColor(R.color.cardview_light_background));
                                }
                            }


                        }
                    },ActionMode.TYPE_FLOATING);
                }
                return true;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_list, null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.customer_name)
        TextView customer_name;

        @BindView(R.id.company_name)
        TextView company_name;

        @BindView(R.id.card_view)
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void deleteCustomer(){

        mContext.getContentResolver().delete(InventoryProvider.Persons.PERSONS_URI,PersonTable._ID + "="+
                mCursor.getString(mCursor.getColumnIndex(PersonTable._ID)),null);
        mContext.getContentResolver().delete(InventoryProvider.Addreses.ADDRESSES_URI, AddressTable.PERSON_ID +
                "="+ mCursor.getString(mCursor.getColumnIndex(PersonTable._ID)),null);
        notifyDataSetChanged();
    }
}
