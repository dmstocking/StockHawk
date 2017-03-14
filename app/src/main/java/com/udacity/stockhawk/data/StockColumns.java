package com.udacity.stockhawk.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface StockColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(TEXT) @NotNull @Unique(onConflict = REPLACE) String SYMBOL = "symbol";
    @DataType(REAL) @NotNull String PRICE = "price";
    @DataType(REAL) @NotNull String ABSOLUTE_CHANGE = "absolute_change";
    @DataType(REAL) @NotNull String PERCENTAGE_CHANGE = "percentage_change";
}
