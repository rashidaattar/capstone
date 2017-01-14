package com.udacity.capstone.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rashida on 30-12-2016.
 */

public class OrderProductDAO implements Parcelable {

    public String prod_name;
    public String prod_quantity;

    public OrderProductDAO(Parcel in) {
        prod_name = in.readString();
        prod_quantity = in.readString();
    }

    public OrderProductDAO(String prod_name, String prod_quantity) {
        this.prod_name = prod_name;
        this.prod_quantity = prod_quantity;
    }

    public static final Creator<OrderProductDAO> CREATOR = new Creator<OrderProductDAO>() {
        @Override
        public OrderProductDAO createFromParcel(Parcel in) {
            return new OrderProductDAO(in);
        }

        @Override
        public OrderProductDAO[] newArray(int size) {
            return new OrderProductDAO[size];
        }
    };

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_quantity() {
        return prod_quantity;
    }

    public void setProd_quantity(String prod_quantity) {
        this.prod_quantity = prod_quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prod_name);
        dest.writeString(prod_quantity);
    }
}
