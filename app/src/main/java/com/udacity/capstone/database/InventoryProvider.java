package com.udacity.capstone.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.Table;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Rashida on 03-11-2016.
 */


@ContentProvider(authority = InventoryProvider.AUTHORITY, database = InventoryDatabase.class,
        packageName =  "com.udacity.capstone.provider")
public class InventoryProvider {



    public static final String AUTHORITY = "com.udacity.capstone";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String PRODUCTS = "products";
        String ORDERS = "orders";
        String PERSONS = "persons";
        String ADDRESSES = "addresses";
        String ORDER_PRODUCT = "order_product";
     //   String CUSTOMER_JOIN = "customer_join";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = InventoryDatabase.PRODUCTS) public static class Products {

        @ContentUri(
                path = Path.PRODUCTS,
                type = "vnd.android.cursor.dir/products",
                defaultSort = ProductTable.PRODUCT_NAME + " ASC")
        public static final Uri PRODUCTS_URI = buildUri(Path.PRODUCTS);
    }


    @TableEndpoint(table = InventoryDatabase.ORDERS) public static class Orders {

        @ContentUri(
                path = Path.ORDERS,
                type = "vnd.android.cursor.dir/orders",
                defaultSort = OrdersTable.ORDER_NUMBER + " ASC")
        public static final Uri ORDERS_URI = buildUri(Path.ORDERS);

        @ContentUri(
                path = Path.ORDERS+Path.ORDER_PRODUCT,
                type = "vnd.android.cursor.dir",
                join = "INNER JOIN "+InventoryDatabase.ORDER_PRODUCT +" ON "+InventoryDatabase.ORDERS+"."
                        +OrdersTable._ID+" = "+InventoryDatabase.ORDER_PRODUCT+"."+Order_ProductTable.ORDER_ID,
                defaultSort = OrdersTable.ORDER_NUMBER + " ASC")
        public static final Uri ORDERS_PRODUCT_JOIN = buildUri(Path.ORDERS+Path.ORDER_PRODUCT);
    }

    @TableEndpoint(table = InventoryDatabase.PERSONS) public static class Persons {

        @ContentUri(
                path = Path.PERSONS,
                type = "vnd.android.cursor.dir/persons",
                defaultSort = PersonTable.PERSON_NAME + " ASC")
        public static final Uri PERSONS_URI = buildUri(Path.PERSONS);


       /* @InexactContentUri(
                name = "PERSON_ID",
                path = Path.PERSONS + "id",
                type = "vnd.android.cursor.item/persons",
                whereColumn = PersonTable._ID,
                pathSegment = 1)
        public static Uri forPersonType(String id) {
            return buildUri(Path.PERSONS, id);
        }*/


        @ContentUri(
                path = Path.PERSONS + Path.ADDRESSES,
                type = "vnd.example.dir",
                where = AddressTable.ADDRESS_TYPE+" like '"+AddressTable.ADDRESS_BILLING+"'",
                join = "INNER JOIN "+ InventoryDatabase.ADRESSES + " ON " + InventoryDatabase.PERSONS + "." + PersonTable._ID + " = "
                        + InventoryDatabase.ADRESSES + "." + AddressTable.PERSON_ID)
        public static final Uri PERSONS_JOIN_URI = buildUri(Path.PERSONS+Path.ADDRESSES);

    }

    @TableEndpoint(table = InventoryDatabase.ADRESSES) public static class Addreses{

        @ContentUri(
                path = Path.ADDRESSES,
                type = "vnd.android.cursor.dir/addresses",
                defaultSort = AddressTable._ID + " ASC")
        public static final Uri ADDRESSES_URI = buildUri(Path.ADDRESSES);

    }

    @TableEndpoint(table = InventoryDatabase.ORDER_PRODUCT) public static class OrderProduct{

        @ContentUri(
                path = Path.ORDER_PRODUCT,
                type = "vnd.android.cursor.dir/order_product",
                defaultSort = Order_ProductTable._ID + " ASC")
        public static final Uri ORDER_PRODUCT_URI = buildUri(Path.ORDER_PRODUCT);


        @ContentUri(
                path = Path.ORDER_PRODUCT+Path.ORDERS,
                type = "vnd.android.cursor.dir",
                join = "LEFT JOIN "+InventoryDatabase.ORDERS +" ON "+InventoryDatabase.ORDERS+"."
                        +OrdersTable._ID+" = "+InventoryDatabase.ORDER_PRODUCT+"."+Order_ProductTable.ORDER_ID+
                        " INNER JOIN "+InventoryDatabase.ADRESSES+" ON "+InventoryDatabase.ADRESSES+"."
                        +AddressTable._ID+" = "+ InventoryDatabase.ORDERS+"."+OrdersTable.ADDRESS_ID+
                        " INNER JOIN "+InventoryDatabase.PERSONS+" ON "+InventoryDatabase.ADRESSES+"."
                        +AddressTable.PERSON_ID+" = "+ InventoryDatabase.PERSONS+"."+PersonTable._ID,
                defaultSort = OrdersTable.ORDER_NUMBER + " ASC")
        public static final Uri ORDERS_PRODUCT_PERSON_JOIN = buildUri(Path.ORDER_PRODUCT+Path.ORDERS);

    }


}


