package com.udacity.stockhawk.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.Table;

import static com.udacity.stockhawk.data.StocksDatabase.DATABASE_VERSION;

@Database(version = DATABASE_VERSION)
public class StocksDatabase {

    private StocksDatabase() {
    }

    public static final int DATABASE_VERSION = 1;

    @Table(StockColumns.class) @IfNotExists public static final String STOCKS = "stocks";
    @Table(HistoryColumns.class) @IfNotExists public static final String HISTORY = "history";
}
