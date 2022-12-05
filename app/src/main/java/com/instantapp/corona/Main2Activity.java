package com.instantapp.corona;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener,AdapterView.OnItemSelectedListener {


    String state;

    Button source;
    LogLatData logLatData;
    Casetimedata casetimedata;
    private LineChart chart;
    private LineChart chart2;
    private LinearLayout success, alert;
    private TextView alertheading, alertdet;
    private TextView statename, statec, stater, stated, statea;
    private TextView totalc, totalr, totald, totala;
    private FirebaseAnalytics mFirebaseAnalytics;



    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String states[] = {"Select" ,
            "Maharashtra" ,
            "Kerala" ,
            "Tamil Nadu" ,
            "Delhi" ,
            "Rajasthan" ,
            "Uttar Pradesh" ,
            "Andhra Pradesh" ,
            "Karnataka" ,
            "Telangana" ,
            "Gujarat" ,
            "Madhya Pradesh" ,
            "Jammu and Kashmir" ,
            "Punjab" ,
            "Haryana" ,
            "West Bengal" ,
            "Bihar" ,
            "Chandigarh" ,
            "Ladakh" ,
            "Assam" ,
            "Andaman and Nicobar Islands" ,
            "Chhattisgarh" ,
            "Uttarakhand" ,
            "Goa" ,
            "Odisha" ,
            "Himachal Pradesh" ,
            "Puducherry" ,
            "Jharkhand" ,
            "Manipur" ,
            "Mizoram" ,
            "Arunachal Pradesh" ,
            "Dadra and Nagar Haveli" ,
            "Daman and Diu" ,
            "Lakshadweep" ,
            "Meghalaya" ,
            "Nagaland" ,
            "Sikkim" ,
            "Tripura"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Bundle extras = getIntent().getExtras();
        state = extras.getString("state");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();



        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        RequestStateReport();

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,states);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);




        //    CheckforDistance(); Done
        //   DisplayNearest(); Done





        RequestCaseTime();

        RequestTotalReport();

        RequestSuccess();


    }

    public void LineChartSetup(float limit) {
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        // add data
        chart.getAxisRight().setEnabled(false);


        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.DEFAULT);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new DateValFor());


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(limit);
        leftAxis.setAxisMinimum(-10f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(false);


    }

    public void LineChart2Setup(float limit) {
        chart2 = findViewById(R.id.chart2);
        chart2.setOnChartValueSelectedListener(this);

        // no description text
        chart2.getDescription().setEnabled(false);

        // enable touch gestures
        chart2.setTouchEnabled(true);

        chart2.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart2.setDragEnabled(true);
        chart2.setScaleEnabled(true);
        chart2.setDrawGridBackground(false);
        chart2.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart2.setPinchZoom(true);

        // set an alternative background color
        chart2.setBackgroundColor(Color.WHITE);

        // add data


        chart2.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart2.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.DEFAULT);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new DateValFor());

        YAxis leftAxis = chart2.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(limit);
        leftAxis.setAxisMinimum(-10f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(false);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            System.exit(0);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_faq) {
            startActivity(new Intent(Main2Activity.this, FAQ.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    private void setData() {


        LineDataSet set1, set2, set3;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(casetimedata.dc());
            set2.setValues(casetimedata.dr());
            set3.setValues(casetimedata.dd());
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(casetimedata.dc(), "Confirmed");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            set2 = new LineDataSet(casetimedata.dr(), "Recovered");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.parseColor("#2db300"));
            set2.setCircleColor(Color.parseColor("#2db300"));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(ColorTemplate.getHoloBlue());
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawCircleHole(false);

            set3 = new LineDataSet(casetimedata.dd(), "Deceased");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.parseColor("#ff0000"));
            set3.setCircleColor(Color.parseColor("#ff0000"));
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setFillAlpha(65);
            set3.setFillColor(ColorTemplate.getHoloBlue());
            set3.setHighLightColor(Color.rgb(244, 117, 117));
            set3.setDrawCircleHole(false);


            // create a dataset and give it a type


            // create a data object with the data sets
            LineData data = new LineData(set1, set2, set3);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            chart.setData(data);
        }
    }

    private void setData2() {


        LineDataSet set1, set2, set3;

        if (chart2.getData() != null &&
                chart2.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart2.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart2.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) chart2.getData().getDataSetByIndex(2);
            set1.setValues(casetimedata.tc());
            set2.setValues(casetimedata.tr());
            set3.setValues(casetimedata.td());
            chart2.getData().notifyDataChanged();
            chart2.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(casetimedata.tc(), "Confirmed");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            set2 = new LineDataSet(casetimedata.tr(), "Recovered");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.parseColor("#2db300"));
            set2.setCircleColor(Color.parseColor("#2db300"));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(ColorTemplate.getHoloBlue());
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            set2.setDrawCircleHole(false);

            set3 = new LineDataSet(casetimedata.td(), "Deceased");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.parseColor("#ff0000"));
            set3.setCircleColor(Color.parseColor("#ff0000"));
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setFillAlpha(65);
            set3.setFillColor(ColorTemplate.getHoloBlue());
            set3.setHighLightColor(Color.rgb(244, 117, 117));
            set3.setDrawCircleHole(false);


            // create a dataset and give it a type


            // create a data object with the data sets
            LineData data = new LineData(set1, set2, set3);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            chart2.setData(data);
        }
    }

    @Override
    public void onNothingSelected() {

    }

    public void RequestLatLogData(final double lati, final double longi) {
        String url = "https://indiadb.000webhostapp.com/loglatfull.php";
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                logLatData = new LogLatData(lati, longi);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    int count = jsonArray.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        logLatData.addrow(jsonObject.getString("PID")
                                , jsonObject.getString("cord")
                                , jsonObject.getString("timefrom")
                                , jsonObject.getString("address")
                                , jsonObject.getString("DataSource")
                        );
                    }
                    CheckForDistance();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, errorListener);

        queue.add(stringRequest);


    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("ACC", "Error :" + error.toString());
        }
    };

    public void CheckForDistance() {
        if (logLatData.near()) {
            DisplayAlertBOX((int) logLatData.lowestt,
                    logLatData.pid.get(logLatData.lowestindex),
                    logLatData.address.get(logLatData.lowestindex),
                    logLatData.date.get(logLatData.lowestindex),
                    logLatData.info.get(logLatData.lowestindex));
        }
    }

    public void RequestStateReport() {
        String url = "https://indiadb.000webhostapp.com/statedet.php?state=" + state;
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    String confirmed = result.getString("Confirmed");
                    String recovered = result.getString("Recovered");
                    String death = result.getString("Deaths");
                    String active = result.getString("Active");
                    DisplayStateReport(state, confirmed, recovered, death, active);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, errorListener);

        queue.add(stringRequest);
    }

    public void RequestStateReport(final String Runtimestate) {
        String url = "https://indiadb.000webhostapp.com/statedet.php?state=" + Runtimestate;
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    String confirmed = result.getString("Confirmed");
                    String recovered = result.getString("Recovered");
                    String death = result.getString("Deaths");
                    String active = result.getString("Active");
                    DisplayStateReport(Runtimestate,confirmed, recovered, death, active);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, errorListener);

        queue.add(stringRequest);
    }

    public void RequestCaseTime() {

        String url = "https://indiadb.000webhostapp.com/casetime.php";
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                casetimedata = new Casetimedata(response);
                LineChartSetup(casetimedata.dailybiggest());
                setData();
                LineChart2Setup(casetimedata.totalbiggest());
                setData2();

            }
        }, errorListener);

        queue.add(stringRequest);

    }


    public void DisplayAlertBOX(int distance, String pid, String addresss, String date,final String info) {
        alertheading = findViewById(R.id.heading);
        alertheading.setText("Nearest reported " + distance + " m away from your location ");

        alertdet = findViewById(R.id.alertdet);
        alertdet.setText("Patient " + pid + " has travelled to " + addresss + " on " + date);

        alert = findViewById(R.id.alertmess);
        alert.setVisibility(View.VISIBLE);

        source = findViewById(R.id.alertsource);
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, com.instantapp.corona.source.class);
                intent.putExtra("info",info);
                startActivity(intent);
            }
        });
    }

    public void DisplayStateReport(String stt ,String c, String r, String d, String a) {
        statename = findViewById(R.id.statename);
        statename.setText(stt);

        statea = findViewById(R.id.stateactive);
        statec = findViewById(R.id.stateconf);
        stater = findViewById(R.id.staterec);
        stated = findViewById(R.id.statedeath);

        statea.setText(a);
        statec.setText(c);
        stater.setText(r);
        stated.setText(d);

    }

    public void RequestTotalReport() {
        String url = "https://indiadb.000webhostapp.com/statedet.php?state=Total";
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject result = new JSONObject(response);
                    String confirmed = result.getString("Confirmed");
                    String recovered = result.getString("Recovered");
                    String death = result.getString("Deaths");
                    String active = result.getString("Active");
                    DisplayTotalReport(confirmed, recovered, death, active);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, errorListener);

        queue.add(stringRequest);
    }

    public void DisplayTotalReport(String c, String r, String d, String a) {
        totala = findViewById(R.id.totalactive);
        totalc = findViewById(R.id.totalconf);
        totalr = findViewById(R.id.totalrec);
        totald = findViewById(R.id.totaldeath);

        totala.setText(a);
        totalc.setText(c);
        totalr.setText(r);
        totald.setText(d);

    }

    public void RequestSuccess() {
        String url = "https://indiadb.000webhostapp.com/success.php";
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (Integer.parseInt(response) == 10) {
                    success = findViewById(R.id.successmessage);
                    success.setVisibility(View.VISIBLE);
                }


            }
        }, errorListener);

        queue.add(stringRequest);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    requestNewLocationData();
                                   RequestLatLogData(location.getLatitude(),location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }


    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            Log.i("GPS", "onLocationResult: "+ mLastLocation.getLatitude() + mLastLocation.getLongitude());

            RequestLatLogData(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    };

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    if(!states[i].matches("Select"))
        RequestStateReport(states[i]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {



    }
}
