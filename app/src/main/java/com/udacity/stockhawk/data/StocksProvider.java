package com.udacity.stockhawk.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = StocksProvider.AUTHORITY, database = StocksDatabase.class)
public final class StocksProvider {

    public static final String AUTHORITY = "com.udacity.stockhawk.stocks.StocksProvider";

    @TableEndpoint(table = StocksDatabase.STOCKS)
    public static class Stocks {

        @ContentUri(
                path = "stocks",
                type = "vnd.android.cursor.dir/stocks",
                defaultSort = StockColumns.SYMBOL + " ASC")
        public static final Uri STOCKS = Uri.parse("content://" + AUTHORITY + "/stocks");

        @InexactContentUri(
                path = "stocks" + "/*",
                name = "STOCK_FROM_SYMBOL",
                type = "vnd.android.cursor.item/stock",
                whereColumn = StockColumns.SYMBOL,
                pathSegment = 1)
        public static Uri withSymbol(String symbol) {
            return Uri.parse("content://" + AUTHORITY + "/stocks/" + symbol);
        }
    }

    @TableEndpoint(table = StocksDatabase.HISTORY)
    public static class Histories {

        @InexactContentUri(
                path = "histories" + "/*",
                name = "HISTORY_FROM_SYMBOL",
                type = "vnd.android.cursor.dir/histories",
                whereColumn = HistoryColumns.SYMBOL,
                pathSegment = 1)
        public static Uri withSymbol(String symbol) {
            return Uri.parse("content://" + AUTHORITY + "/histories/" + symbol);
        }
    }
}
