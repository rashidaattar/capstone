package com.udacity.capstone.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.Check;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by 836137 on 09-11-2016.
 */

public interface AddressTable {

    public String ADDRESS_SHIPPING = "shipping_address";
    public String ADDRESS_BILLING = "billing_Address";


    @DataType(INTEGER) @AutoIncrement @PrimaryKey
    String _ID = "address_id";

    @NotNull @DataType(TEXT)
    String ADDRESS_LINE1 = "address_line1";

    @DataType(TEXT)
    String ADDRESS_LINE2 = "address_line2";

    @DataType(TEXT)
    String CITY = "city";

    @DataType(TEXT)
    String STATE = "state";

    @DataType(TEXT)
    String CONTACT_NO = "contact_no";

    @DataType(INTEGER) @References(table = InventoryDatabase.PERSONS, column = PersonTable._ID)
    String PERSON_ID = "person_id";

    @DataType(TEXT) @Check(AddressTable.ADDRESS_TYPE + " in ('"+AddressTable.ADDRESS_BILLING+"','"+AddressTable.ADDRESS_SHIPPING+"')")
    String ADDRESS_TYPE = "address_type";
}
