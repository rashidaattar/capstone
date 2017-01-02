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
import com.udacity.capstone.activity.CustomerDetailActivity;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.PersonTable;
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 836137 on 09-11-2016.
 */

public class CustomersCursorAdapter extends InventoryCursorAdapter<CustomersCursorAdapter.ViewHolder> {

    public Context mContext;
    public ActionMode mActionMode;
    public Cursor mCursor;
    private boolean isCustomer;


    public CustomersCursorAdapter(Context context, Cursor cursor, boolean isCustomer) {
        super(context, cursor);
        mContext= context;
        mCursor = cursor;
        this.isCustomer = isCustomer;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, final int position) {

        viewHolder.customer_name.setText(cursor.getString(cursor.getColumnIndex(PersonTable.PERSON_NAME)).replace("_"," "));
        viewHolder.company_name.setText(cursor.getString(cursor.getColumnIndex(PersonTable._ID)));
        mCursor=getCursor(); //obtain cursor with updated data
        mCursor.moveToPosition(position); //move cursor to the current position selected
        final String personID = mCursor.getString(mCursor.getColumnIndex(PersonTable._ID));
        final String addressID = mCursor.getString(mCursor.getColumnIndex(AddressTable._ID));
        final String personName = mCursor.getString(mCursor.getColumnIndex(PersonTable.PERSON_NAME));
        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActionMode!=null){
                    mActionMode.finish();
                }
                else{
                    Intent intent = new Intent(mContext, CustomerDetailActivity.class);
                    intent.putExtra(Constants.VIEW_DETAIL,personID);
                    intent.putExtra(Constants.VIEW_CUSTOMER_DETAIL_NAME,personName);
                    mContext.startActivity(intent);

                }
            }

        });

        viewHolder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCursor.moveToPosition(position);
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
                                    Intent intent = new Intent(mContext,AddEditCustomerActivity.class);
                                    intent.putExtra(Constants.EDIT_CUSTOMER_BOOLEAN,true);
                                    intent.putExtra(Constants.EDIT_CUSTOMER_CUSTOMERID,personID);
                                    intent.putExtra(Constants.EDIT_CUSTOMER_ADDRESSID,addressID);
                                    intent.putExtra(Constants.PERSON_TYPE_LABEL,isCustomer);
                                    mContext.startActivity(intent);
                                    mode.finish();
                                    return true;
                                case R.id.delete_button:
                                    deleteCustomer();
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
                                    v.setBackgroundColor(mContext.getColor(R.color.bgColor));
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
        mContext.getContentResolver().notifyChange(InventoryProvider.Persons.PERSONS_URI, null);
        mContext.getContentResolver().notifyChange(InventoryProvider.Addreses.ADDRESSES_URI,null);
        mContext.getContentResolver().notifyChange(InventoryProvider.Persons.PERSONS_JOIN_URI,null);
      //  notifyItemRemoved(position);
    }
}
