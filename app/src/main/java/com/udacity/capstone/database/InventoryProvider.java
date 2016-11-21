package com.udacity.capstone.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by 836137 on 03-11-2016.
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
    }

    @TableEndpoint(table = InventoryDatabase.PERSONS) public static class Persons {

        @ContentUri(
                path = Path.PERSONS,
                type = "vnd.android.cursor.dir/persons",
                defaultSort = PersonTable.PERSON_NAME + " ASC")
        public static final Uri PERSONS_URI = buildUri(Path.PERSONS);


       /* @InexactContentUri(
                name = "TYPE_OF_PERSON",
                path = Path.PERSONS + "*//*",
                type = "vnd.android.cursor.item/persons",
                whereColumn = PersonTable.PERSON_TYPE,
                pathSegment = 1)
        public static Uri forPersonType(String type) {
            return buildUri(Path.PERSONS, type);
        }*/


        @ContentUri(
                path = Path.PERSONS + "/*",
                type = "vnd.example.dir.item/persons" ,
                join = "JOIN "+ InventoryDatabase.ADRESSES + " ON " + InventoryDatabase.PERSONS + "." + PersonTable._ID + " = "
                        + InventoryDatabase.ADRESSES + "." + AddressTable.PERSON_ID)
        public static final Uri PERSONS_JOIN_URI = buildUri(Path.PERSONS);

    }

    @TableEndpoint(table = InventoryDatabase.ADRESSES) public static class Addreses{

        @ContentUri(
                path = Path.ADDRESSES,
                type = "vnd.android.cursor.dir/persons",
                defaultSort = AddressTable._ID + " ASC")
        public static final Uri ADDRESSES_URI = buildUri(Path.ADDRESSES);

    }
}


