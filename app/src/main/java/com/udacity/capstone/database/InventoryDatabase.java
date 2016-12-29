package com.udacity.capstone.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by 836137 on 03-11-2016.
 */


@Database(version = InventoryDatabase.VERSION,
        packageName =  "com.udacity.capstone.provider")
public class InventoryDatabase {

    public static final int VERSION = 1;


    @Table(OrdersTable.class) public static final String ORDERS = "orders";

    @Table(ProductTable.class) public static final String PRODUCTS = "products";

    @Table(PersonTable.class) public static final String PERSONS = "persons";

    @Table(AddressTable.class) public static final String ADRESSES = "addresses";

    @Table(Order_ProductTable.class) public static final String ORDER_PRODUCT = "order_product";

}

