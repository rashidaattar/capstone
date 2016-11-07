package com.udacity.capstone.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by 836137 on 03-11-2016.
 */


@ContentProvider(authority = InventoryProvider.AUTHORITY, database = InventoryDatabase.class,
        packageName =  "com.udacity.capstone.provider")
public class InventoryProvider {


    public static final String AUTHORITY = "com.udacity.capstone";


    @TableEndpoint(table = InventoryDatabase.PRODUCTS) public static class Products {

        @ContentUri(
                path = "products",
                type = "vnd.android.cursor.dir/products",
                defaultSort = ProductTable.PRODUCT_NAME + " ASC")
        public static final Uri PRODUCTS_URI = Uri.parse("content://" + AUTHORITY + "/products");
    }


    @TableEndpoint(table = InventoryDatabase.ORDERS) public static class Orders {

        @ContentUri(
                path = "orders",
                type = "vnd.android.cursor.dir/orders",
                defaultSort = OrdersTable.ORDER_NUMBER + " ASC")
        public static final Uri ORDERS_URI = Uri.parse("content://" + AUTHORITY + "/orders");
    }
}


