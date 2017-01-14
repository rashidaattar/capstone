package com.udacity.capstone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.activity.AddEditOrderActivity;
import com.udacity.capstone.activity.AddEditProductActivity;
import com.udacity.capstone.activity.OrderDetailActivity;
import com.udacity.capstone.database.AddressTable;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.Order_ProductTable;
import com.udacity.capstone.database.OrdersTable;
import com.udacity.capstone.database.ProductTable;
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
 * Created by Rashida on 30-12-2016.
 */

public class OrdersCursorAdapter extends InventoryCursorAdapter<OrdersCursorAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private HashMap<Integer,ArrayList<OrderProductDAO>> mMap = new HashMap<>();
    private ArrayList<OrderProductDAO> productsList;
    public ActionMode mActionMode;


    public OrdersCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mCursor = cursor;
        mContext=context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor, final int position) {
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
        viewHolder.order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActionMode!=null){
                    mActionMode.finish();
                }
                else{
                    getCursor().moveToPosition(position);
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, (View)viewHolder.order_no, mContext.getString(R.string.first_transition));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mContext.startActivity(createIntent(intent),options.toBundle());
                    }
                    else{
                        mContext.startActivity(createIntent(intent));
                    }
                }

            }
        });

        viewHolder.order_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getCursor().moveToPosition(position);
                    v.setBackgroundColor(mContext.getColor(R.color.cardview_dark_background));
                    mActionMode=v.startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater inflater = mode.getMenuInflater();
                            inflater.inflate(R.menu.edit_delete_contextmenu, menu);
                            mode.getMenu().getItem(1).setVisible(false);
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
                                    Intent intent = new Intent(mContext,AddEditOrderActivity.class);
                                    intent.putExtra(Constants.EDIT_ORDER_BOOLEAN,true);
                                    mContext.startActivity(createIntent(intent));
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list, null);
        return new ViewHolder(view);
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

        @BindView(R.id.card_view)
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private Intent createIntent(Intent intent){
        int order_id =getCursor().getInt(getCursor().getColumnIndex(Order_ProductTable.ORDER_ID));
        ArrayList<OrderProductDAO> orderProductDAOs = new ArrayList<>();
        Set<Map.Entry<Integer, ArrayList<OrderProductDAO>>> entrySet = mMap.entrySet();
        for (Map.Entry entry : entrySet)
        {
            if((Integer)entry.getKey()== order_id){
                orderProductDAOs.addAll((ArrayList<OrderProductDAO>)entry.getValue());
            }
        }

        intent.putExtra(Constants.ORDER_ID_EXTRA,order_id);
        intent.putExtra(Constants.PRODUCT_NAME_EXTRA,orderProductDAOs);
        return intent;
    }


}
