package com.udacity.capstone.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.Check;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Rashida on 03-11-2016.
 */

public interface OrdersTable {

    String STATUS_COMPLETED = "COMPLETED";
    String STATUS_PROGRESS = "IN-PROGRESS";
    String STATUS_CANCELLED = "CANCELLED";


    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(TEXT) @NotNull
    String ORDER_NUMBER = "order_number";

    @DataType(TEXT) @NotNull
    String ORDER_DATE = "order_date";

    @DataType(TEXT) @NotNull
    String DELIVERY_DATE = "delivery_date";

    @DataType(REAL) @NotNull
    String ORDER_AMOUNT = "order_amount";

    @DataType(TEXT) @NotNull //@Check(OrdersTable.ORDER_STATUS + " in " + " ('"+ OrdersTable.STATUS_CANCELLED+"','"+OrdersTable.STATUS_COMPLETED+"','"+OrdersTable.STATUS_PROGRESS+"') ")
    String ORDER_STATUS = "status";

    @DataType(INTEGER) @References(table = InventoryDatabase.ADRESSES, column = AddressTable._ID)
    String ADDRESS_ID = "address_id";

    /*@DataType(INTEGER) @References(table = InventoryDatabase.PERSONS, column = PersonTable._ID)
    String PERSON_ID = "person_id";*/


}
