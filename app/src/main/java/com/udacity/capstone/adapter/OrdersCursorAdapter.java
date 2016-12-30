package com.udacity.capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.activity.OrderDetailActivity;
import com.udacity.capstone.database.Order_ProductTable;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.util.Constants;
import com.udacity.capstone.util.OrderProductDAO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 836137 on 30-12-2016.
 */

public class OrdersCursorAdapter extends InventoryCursorAdapter<OrdersCursorAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private HashMap<Integer,ArrayList<OrderProductDAO>> mMap = new HashMap<>();
    private ArrayList<OrderProductDAO> productsList;


    public OrdersCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mCursor = cursor;
        mContext=context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, final int position) {
        Log.d("TAG","in binviewholder"+"size of cursor ----"+cursor.getCount());
        cursor.moveToPosition(position);
        OrderProductDAO orderProductDAO = new OrderProductDAO(cursor.getString(cursor.getColumnIndex(Order_ProductTable.PRODUCT_NAME)),
                cursor.getString(cursor.getColumnIndex(Order_ProductTable.PRODUCT_QUANTITY)));
        if(mMap.containsKey(cursor.getInt(cursor.getColumnIndex(Order_ProductTable.ORDER_ID)))){
            productsList.add(orderProductDAO);
            mMap.put(cursor.getInt((cursor.getColumnIndex(Order_ProductTable.ORDER_ID)))
                    ,productsList);
            viewHolder.order_layout.setVisibility(View.GONE);
        }
        else{
            productsList = new ArrayList<>();
            productsList.add(orderProductDAO);
            mMap.put(cursor.getInt((cursor.getColumnIndex(Order_ProductTable.ORDER_ID)))
                    ,productsList);
            viewHolder.order_no.setText(cursor.getString(cursor.getColumnIndex(OrdersTable.ORDER_NUMBER)));
            viewHolder.delivery_date.setText(cursor.getString(cursor.getColumnIndex(OrdersTable.DELIVERY_DATE)));
            viewHolder.order_status.setText(cursor.getString(cursor.getColumnIndex(OrdersTable.ORDER_STATUS)));
        }
        viewHolder.prod_name.setText(cursor.getString(cursor.getColumnIndex(Order_ProductTable.PRODUCT_NAME)));
        viewHolder.prod_quantity.setText(cursor.getString(cursor.getColumnIndex(Order_ProductTable.PRODUCT_QUANTITY)));
        viewHolder.prod_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getCursor().moveToPosition(position);
                int order_id =getCursor().getInt(getCursor().getColumnIndex(Order_ProductTable.ORDER_ID));
                ArrayList<OrderProductDAO> orderProductDAOs = new ArrayList<>();
                Set<Map.Entry<Integer, ArrayList<OrderProductDAO>>> entrySet = mMap.entrySet();
                for (Map.Entry entry : entrySet)
                {
                    if((Integer)entry.getKey()== order_id){
                       orderProductDAOs.addAll((ArrayList<OrderProductDAO>)entry.getValue());
                    }
                }
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra(Constants.ORDER_ID_EXTRA,order_id);
                intent.putExtra(Constants.PRODUCT_NAME_EXTRA,orderProductDAOs);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("TAG","in createviewholder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_main)
        RelativeLayout order_layout;

        @BindView(R.id.order_number)
        TextView order_no;

        @BindView(R.id.delivery_date)
        TextView delivery_date;

        @BindView(R.id.product_name)
        TextView prod_name;

        @BindView(R.id.product_quantity)
        TextView prod_quantity;

        @BindView(R.id.order_status)
        TextView order_status;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
