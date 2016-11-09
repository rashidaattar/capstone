package com.udacity.capstone.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.Check;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 836137 on 09-11-2016.
 */

public interface PersonTable {

    String CUSTOMER_PERSON = "customer";
    String VENDOR_PERSON = "vendor";

    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(TEXT) @NotNull
    String PERSON_NAME = "name";

    @DataType(TEXT)
    String COMPANY_NAME = "company_name";

    @DataType(TEXT) @NotNull
    String CONTACT_NO = "contact_number";

    @DataType(TEXT)
    String EMAIL = "email";

    @DataType(TEXT) @NotNull @Check(PersonTable.PERSON_TYPE + " in " + " ('"+ PersonTable.CUSTOMER_PERSON+"','"+PersonTable.VENDOR_PERSON+"') ")
    String PERSON_TYPE = "person_type";

}
