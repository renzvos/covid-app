package com.instantapp.corona;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    int Netversion = 3;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        setTitle("Getting Started");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);




    }

    public void CallforSignIn(final String state) {

        String url = "https://indiadb.000webhostapp.com/login.php";
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("ACC",  response );

                if (Netversion >= Integer.parseInt(response)) {
                    Intent i;
                    i = new Intent(MainActivity.this,Main2Activity.class);
                    i.putExtra("state",state);
                   startActivity(i);
                } else {

                    mTextMessage.setText("Please update your app");
                }

            }
        }, errorListener);

        queue.add(stringRequest);
    }




    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("ACC","Error :" + error.toString());
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mTextMessage.setText("Getting Location");
                    getState();


                } else {

                  mTextMessage.setText("Location Access is Required");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

   public void getState()
   {
       String url = "http://api.ipstack.com/check?access_key=6dfdc5a11739c001e22b00c211a50694";
       VolleyLog.DEBUG = true;
       RequestQueue queue = Volley.newRequestQueue(this);
       StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               Log.i("ACC",  response );
               JSONObject res;
               try {
                   res = new JSONObject(response);
                   String state = res.getString("region_name");
                   CallforSignIn(state);

               } catch (JSONException e) {
                   e.printStackTrace();
               }



           }
       }, errorListener);

       queue.add(stringRequest);

   }


}

