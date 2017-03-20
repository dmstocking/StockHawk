package com.udacity.stockhawk.ui.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.data.StockColumns;
import com.udacity.stockhawk.data.StocksProvider;

import net.simonvt.schematic.annotation.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    @NotNull private final ContentResolver contentResolver;
    @NotNull private final Context context;
    @NotNull private final String packageName;

    private final DecimalFormat dollarFormat;
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat percentageFormat;

    private Cursor data;

    public StockRemoteViewsFactory(ContentResolver contentResolver,
                                   Context context,
                                   String packageName) {
        this.contentResolver = contentResolver;
        this.context = context;
        this.packageName = packageName;

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        data = contentResolver.query(StocksProvider.Stocks.STOCKS,
                                     null,
                                     null,
                                     null,
                                     null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.getCount();
        }

        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null ||
                !data.moveToPosition(position)) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.widget_list_item_quote);
        remoteViews.setTextViewText(R.id.symbol, data.getString(data.getColumnIndex(StockColumns.SYMBOL)));
        remoteViews.setTextViewText(R.id.price, dollarFormat.format(data.getFloat(data.getColumnIndex(StockColumns.PRICE))));

        float rawAbsoluteChange = data.getFloat(data.getColumnIndex(StockColumns.ABSOLUTE_CHANGE));
        float percentageChange = data.getFloat(data.getColumnIndex(StockColumns.PERCENTAGE_CHANGE));

        if (rawAbsoluteChange > 0) {
            remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }

        String change = dollarFormatWithPlus.format(rawAbsoluteChange);
        String percentage = percentageFormat.format(percentageChange / 100);

        if (PrefUtils.getDisplayMode(context)
                .equals(context.getString(R.string.pref_display_mode_absolute_key))) {
            remoteViews.setTextViewText(R.id.change, change);
        } else {
            remoteViews.setTextViewText(R.id.change, percentage);
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(packageName, R.layout.widget_list_item_quote);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (data.moveToPosition(position)) {
            return data.getLong(data.getColumnIndex(StockColumns._ID));
        }
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
