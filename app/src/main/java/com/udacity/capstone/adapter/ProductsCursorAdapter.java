package com.udacity.capstone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.capstone.R;
import com.udacity.capstone.activity.AddEditProductActivity;
import com.udacity.capstone.activity.ProductsDetailActivity;
import com.udacity.capstone.database.InventoryProvider;
import com.udacity.capstone.database.ProductTable;
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rashida on 08-11-2016.
 */

public class ProductsCursorAdapter extends InventoryCursorAdapter<ProductsCursorAdapter.ViewHolder> {


   // private Cursor mCursor;
    private Context mContext;

    public ActionMode mActionMode;

    public ProductsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext=context;
     //   this.mCursor=cursor;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor,  int position) {

        cursor.moveToPosition(position);
        viewHolder.product_name.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_NAME)));
        viewHolder.product_desc.setText(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_DESCRIPTION)));
        viewHolder.prod_quant.setText(mContext.getResources().getString(R.string.quantity)+" : "+cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_QUANTITY)));
        final int THUMBSIZE = 64;
        if(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_IMG))!=null && cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_IMG)).length()>0){
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail
                    (BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(ProductTable.PRODUCT_IMG))),
                            THUMBSIZE, THUMBSIZE);
            viewHolder.thumbnail_view.setImageBitmap(ThumbImage);
        }
        else{
            viewHolder.thumbnail_view.setVisibility(View.GONE);
        }


     //   mCursor = getCursor();

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mActionMode!=null){
                        mActionMode.finish();
                    }
                    else{
                        getCursor().moveToPosition(viewHolder.getAdapterPosition());
                        Intent intent = new Intent(mContext, ProductsDetailActivity.class);
                        intent.putExtra(Constants.VIEW_DETAIL,getCursor().getString(getCursor().getColumnIndex(ProductTable._ID)));
                        intent.putExtra(Constants.PRODUCT_NAME_EXTRA,getCursor().getString(getCursor().getColumnIndex(ProductTable.PRODUCT_NAME)));
                        Pair<View, String> p1 = Pair.create((View)viewHolder.product_name, mContext.getString(R.string.first_transition));
                        Pair<View, String> p2 = Pair.create((View)viewHolder.thumbnail_view, mContext.getString(R.string.second_transition));
                        /*ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, (View)viewHolder.product_name, mContext.getString(R.string.first_transition));*/
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, p1,p2);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mContext.startActivity(intent,options.toBundle());
                        }
                        else{
                            mContext.startActivity(intent);
                        }
                    }

                }
            });
        viewHolder.card_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getCursor().moveToPosition(viewHolder.getAdapterPosition());
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
                                        Intent intent = new Intent(mContext,AddEditProductActivity.class);
                                        intent.putExtra(Constants.EDIT_PRODUCT_BOOLEAN,true);
                                        intent.putExtra(Constants.EDIT_PRODUCTS,getCursor().getString(getCursor().getColumnIndex(ProductTable._ID)));
                                        mContext.startActivity(intent);
                                        mode.finish();
                                        return true;
                                    case R.id.delete_button:
                                        deleteProduct();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, null);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_name)
        TextView product_name;
        @BindView(R.id.product_desc)
        TextView product_desc;
        @BindView(R.id.thumbnails)
        ImageView thumbnail_view;
        @BindView(R.id.card_view)
        CardView card_view;
        @BindView(R.id.product_quantity)
        TextView prod_quant;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void deleteProduct() {

        mContext.getContentResolver().delete(InventoryProvider.Products.PRODUCTS_URI, ProductTable._ID + "=" +
                getCursor().getString(getCursor().getColumnIndex(ProductTable._ID)), null);
        notifyDataSetChanged();
        mContext.getContentResolver().notifyChange(InventoryProvider.Products.PRODUCTS_URI, null);
    }
}
