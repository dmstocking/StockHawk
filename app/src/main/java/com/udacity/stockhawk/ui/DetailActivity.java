package com.udacity.stockhawk.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.HistoryColumns;
import com.udacity.stockhawk.data.StocksProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_LOADER = 1;

    @BindView(R.id.chart) LineChart lineChart;

    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        symbol = getIntent().getStringExtra("symbol");
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                                StocksProvider.Histories.withSymbol(symbol),
                                null,
                                null,
                                null,
                                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() != 0) {
//            error.setVisibility(View.VISIBLE);
        }


        List<Entry> entires = new ArrayList<>();
        while (data.moveToNext()) {
            float price = data.getFloat(data.getColumnIndex(HistoryColumns.PRICE));
            long date = data.getLong(data.getColumnIndex(HistoryColumns.DATE));
            entires.add(new Entry(date, price));
        }

        if (entires.size() != 0) {
            LineDataSet dataSet = new LineDataSet(entires, "Closing Price");
            dataSet.setColors(new int[]{R.color.colorAccent}, getApplicationContext());
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.getAxisLeft().setTextColor(R.color.white);
        } else {
            lineChart.clear();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

