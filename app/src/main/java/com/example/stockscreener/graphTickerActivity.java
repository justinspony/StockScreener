package com.example.stockscreener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.klim.tcharts.entities.ChartData;
import com.klim.tcharts.entities.ChartItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;


public class graphTickerActivity extends AppCompatActivity {

    LineChart tickerChart;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);

        tickerChart = findViewById(R.id.chart1);


        Intent graphIntent = getIntent();
        Bundle args = graphIntent.getBundleExtra("BUNDLE");
        ArrayList<Double> tickerList = (ArrayList<Double>) args.getSerializable("Chosen Ticker");
        ArrayList<Entry> values = new ArrayList<Entry>();
        //LinkedHashMap<Integer,Double> x_y =new LinkedHashMap<Integer,Double>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        int i = 0;
        for(Double t : tickerList){
            Log.d("Price", t.toString());
            //x_y.put(i, t);

            values.add( new Entry(i, t.floatValue()));
            i += 1;
        } // end for

        /*   */
        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        tickerChart.setData(data);
        tickerChart.invalidate();

    }
}
