package com.udacity.capstone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.database.PersonTable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 836137 on 09-11-2016.
 */

public class CustomersCursorAdapter extends InventoryCursorAdapter<CustomersCursorAdapter.ViewHolder> {


    public CustomersCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        viewHolder.customer_name.setText(cursor.getString(cursor.getColumnIndex(PersonTable.PERSON_NAME)));
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
