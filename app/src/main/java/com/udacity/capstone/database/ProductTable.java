package com.udacity.capstone.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.BLOB;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Rashida on 03-11-2016.
 */

public interface ProductTable {

    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(TEXT) @NotNull
    String PRODUCT_NAME = "product_name";

    @DataType(TEXT)
    String PRODUCT_CODE = "product_code";

    @DataType(REAL) @NotNull
    String PRODUCT_QUANTITY = "product_quantity";

    @DataType(TEXT) @NotNull
    String PRODUCT_METRIC = "product_metric";

    @DataType(TEXT)
    String PRODUCT_DESCRIPTION = "product_description";

    @DataType(TEXT)
    String PRODUCT_DIMENSION = "product_dimension";

    @DataType(REAL)
    String MINIMUM_QUANTITY = "minimum_quantity";

    @DataType(TEXT)
    String PRODUCT_IMG = "product_img";

}
