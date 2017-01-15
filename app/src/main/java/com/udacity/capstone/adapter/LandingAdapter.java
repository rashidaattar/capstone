package com.udacity.capstone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.udacity.capstone.R;
import com.udacity.capstone.activity.CustomerListActivity;
import com.udacity.capstone.activity.OrderListActivity;
import com.udacity.capstone.activity.ProductListActivity;
import com.udacity.capstone.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rashida on 11-01-2017.
 */

public class LandingAdapter extends RecyclerView.Adapter<LandingAdapter.ViewHolder>  {

    private Context mContext;
    private InterstitialAd mInterstitialAd;

    public LandingAdapter(Context mContext, InterstitialAd mInterstitialAd) {
        this.mContext = mContext;
        this.mInterstitialAd = mInterstitialAd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.landing_item, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        switch(position){
            case 0 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.landing_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_inventory));
                }
                holder.landing_txt.setText(R.string.inventory_label);
                break;

            case 1 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.landing_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_customer));
                }
                holder.landing_txt.setText(R.string.customers_label);
                break;
            case 2 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.landing_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_vendor));
                }
                holder.landing_txt.setText(R.string.vendors_label);
                break;

            case 3 :
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.landing_img.setImageDrawable(mContext.getDrawable(R.drawable.ic_order));
                }
                holder.landing_txt.setText(R.string.orders_label);
                break;

        }
        holder.landing_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntent(position);
            }
        });

    }

    private void launchIntent(int position) {

        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
        Intent intent = null;
        switch(position){
            
            case 0 :
                intent = new Intent(mContext, ProductListActivity.class);
                break;
            
            case 1 :
                intent = new Intent(mContext, CustomerListActivity.class);
                intent.putExtra(Constants.PERSON_TYPE_LABEL, Constants.CUSTOMER_TYPE);
                break;
            
            case 2 :
                intent = new Intent(mContext,CustomerListActivity.class);
                intent.putExtra(Constants.PERSON_TYPE_LABEL,Constants.VENDOR_TYPE);
                break;
            
            case 3 :
                intent = new Intent(mContext, OrderListActivity.class);
                break;
        }
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.landing_img)
        ImageView landing_img;

        @BindView(R.id.landing_txt)
        TextView landing_txt;

        @BindView(R.id.card_view)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            landing_txt.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"FjallaOne-Regular.ttf"));
        }
    }
}
