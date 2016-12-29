package com.udacity.capstone.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 836137 on 28-12-2016.
 */

public interface Order_ProductTable {

    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(INTEGER) @NotNull
    String ORDER_ID = "order_id";

    @DataType(INTEGER) @NotNull
    String PRODUCT_NAME = "product_name";

    @DataType(REAL) @NotNull
    String PRODUCT_QUANTITY = "product_quantity";
}
