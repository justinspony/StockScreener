package com.example.stockscreener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity /*implements  AdapterView.OnItemSelectedListener */ {

    String chosenTicker = "";
    String chosenDate = "";
    ArrayList<Ticker> tickObjList = new ArrayList<>();
    ArrayList<Ticker> chosenTickerList = new ArrayList<>();
    ArrayList<Double> chosenTickerPriceList = new ArrayList<>();

    HashSet<String> tickerNameSet = new HashSet<>();
    HashSet<String> tickerDateSet = new HashSet<>();
    RequestQueue requestQueue;

    ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pBar = (ProgressBar) findViewById(R.id.progressBar);

        Button graphButton;
        Button stockDataButton;
        Button defButton;
        graphButton = findViewById(R.id.toGraph);
        defButton = findViewById(R.id.defButton);
        stockDataButton = findViewById(R.id.stockDataButtonID);

        requestQueue = Volley.newRequestQueue(this);
        jsonParse();

        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGraph();
            }
        });

        stockDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockData();
            }
        });

        defButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefinitions();
            }
        });

    } // end onCreate

    public void showDefinitions(){
        Intent showDefinitionsIntent = new Intent(this, marketTermDefinitions.class);
        startActivity(showDefinitionsIntent);
    }

    public void showGraph(){
        for(Ticker t : tickObjList){
            if(t.getTicker().equals(chosenTicker)){
                chosenTickerList.add(t);
                chosenTickerPriceList.add(t.getClose());
            }
        }
        Intent showGraphIntent = new Intent(this, graphTickerActivity.class);
        Bundle extras = new Bundle();

        extras.putSerializable("Chosen Ticker", (Serializable) chosenTickerPriceList);
        showGraphIntent.putExtra("BUNDLE", extras);
        startActivity(showGraphIntent);
    }

    private void  jsonParse(){

        String url = "https://www.quandl.com/api/v3/datatables/SHARADAR/SEP.json?api_key=ufbdsyreuGemgDeEfsZH";
        JsonObjectRequest requestObj = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            pBar.setVisibility(View.VISIBLE);

                            JSONObject mObj = response.getJSONObject("datatable");
                            JSONArray tickersArr = mObj.getJSONArray("data");

                            Log.d("Check Table", mObj.get("data").toString());

                            for (int i = 0; i < tickersArr.length(); i++) {

                                Log.d("Check Obj Output", tickersArr.get(i).toString());
                                JSONArray tickerElem = tickersArr.getJSONArray(i);

                                /* */
                                String tick = tickerElem.get(0).toString();
                                Log.d("Ticker Elem 0", tickerElem.get(0).toString());

                                String date = tickerElem.get(1).toString();
                                Log.d("Ticker Elem 1", tickerElem.get(1).toString());

                                double open = (Double) tickerElem.get(2);
                                Log.d("Ticker Elem 2", tickerElem.get(2).toString());

                                double high = (Double) tickerElem.get(3);
                                Log.d("Ticker Elem 3", tickerElem.get(3).toString());

                                double low = (Double) tickerElem.get(4);
                                Log.d("Ticker Elem 4", tickerElem.get(4).toString());

                                double close = (Double) tickerElem.get(5);
                                Log.d("Ticker Elem 5", tickerElem.get(5).toString());

                                int volume = ((Double) tickerElem.get(6)).intValue();
                                Log.d("Ticker Elem 6", String.valueOf(volume));

                                double closeAdj = (Double) tickerElem.get(7);
                                Log.d("Ticker Elem 7", tickerElem.get(7).toString());

                                double closeUnAdj = (Double) tickerElem.get(8);
                                Log.d("Ticker Elem 8", tickerElem.get(8).toString());

                                String lastUpdated = tickerElem.get(9).toString();
                                Log.d("Ticker Elem 9", tickerElem.get(9).toString());

                                Ticker ticker = new Ticker(tick, date, open, high, low, close, volume,
                                        closeAdj, closeUnAdj, lastUpdated);

                                Log.d("Add to Array List", ticker.toString());
                                //tickerSet.add(ticker.getTicker()); // use set of tickers for Ticker Drop Down list

                                tickObjList.add(ticker);

                                Log.d("Check list", tickObjList.get(0).getTicker());

                            } // end for loop

                            pBar.setVisibility(View.INVISIBLE);

                            initializeSpinner(tickObjList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Debug Test Execption", "Exception Thrown On Debug Test");
                        }

                    }  // end onResponse method
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("On Error Volley Error", "Error: " + error.toString());
                        Toast toast=Toast.makeText(getApplicationContext(),"Connection to data source failed. Restart the application and try again",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }); // end JSON Request object
        requestQueue.add(requestObj);

        //return tickObjList;
    }

/**/
    private void initializeSpinner(ArrayList<Ticker> tickerCompleteList){

        for(Ticker t: tickerCompleteList){
            tickerNameSet.add(t.getTicker());
            tickerDateSet.add(t.getDate());
        }

        Spinner spinner = (Spinner) findViewById(R.id.tickerSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEST TAG ON ITEM SELECT", parent.getItemAtPosition(position).toString());
                chosenTicker = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner dateSpinner = (Spinner) findViewById(R.id.tickerSpinnerDate);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Date Spinner TAG", parent.getItemAtPosition(position).toString());
                chosenDate = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> tickerList = new ArrayList<String>(tickerNameSet);
        ArrayAdapter<String> tickerSpinAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, tickerList);

        ArrayList<String> dateList = new ArrayList<>(tickerDateSet);
        ArrayAdapter<String> tickerSpinDateAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, dateList);
        dateSpinner.setAdapter(tickerSpinDateAdapter);

        spinner.setAdapter(tickerSpinAdapter);

    } //end initializeSpinner method

    public void getStockData(){

        TextView openPriceTextView = (TextView) findViewById(R.id.openPriceTV);
        TextView closePriceTextView = (TextView) findViewById(R.id.closePriceTV);
        TextView lowPriceTextView = (TextView) findViewById(R.id.lowPriceTV);
        TextView highPriceTextView = (TextView) findViewById(R.id.highPriceTV);
        TextView volumeTextView = (TextView) findViewById(R.id.volumePriceTV);

        for(Ticker t : tickObjList){

            if (t.getTicker().equals(chosenTicker) && t.getDate().equals(chosenDate)){
                openPriceTextView.setText(String.valueOf(t.getOpen()));
                closePriceTextView.setText(String.valueOf(t.getClose()));
                highPriceTextView.setText(String.valueOf(t.getHigh()));
                lowPriceTextView.setText(String.valueOf(t.getLow()));
                volumeTextView.setText(String.valueOf(((Double) t.getVolume()).intValue()));

            }

        }
    } // getStockData

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine which menu option was chosen

        if (item.getItemId() == R.id.action_logout) {
            // Logout selected
            System.exit(0);
            return true;
        }
        else if (item.getItemId() == R.id.action_about) {
            // About selected
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}