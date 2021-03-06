package com.udacity.stockhawk.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.UniqueConstraint;

import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

@UniqueConstraint(
        columns = { HistoryColumns.SYMBOL, HistoryColumns.DATE },
        onConflict = REPLACE)
public interface HistoryColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    @References(table = StocksDatabase.STOCKS, column = StockColumns.SYMBOL)
    String SYMBOL = "symbol";

    @DataType(REAL) @NotNull String PRICE = "price";
    @DataType(INTEGER) @NotNull String DATE = "date";
}
