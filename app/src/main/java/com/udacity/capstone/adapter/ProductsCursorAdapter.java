package com.udacity.capstone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.database.ProductTable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 836137 on 08-11-2016.
 */

public class ProductsCursorAdapter extends InventoryCursorAdapter<ProductsCursorAdapter.ViewHolder> {


    private Cursor mCursor;
    private Context mContext;

    public ProductsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext=context;
        this.mCursor=cursor;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        viewHolder.product_name.setText(cursor.getString(cursor.getColumnIndex(ProductTable._ID)));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_name)
        TextView product_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
