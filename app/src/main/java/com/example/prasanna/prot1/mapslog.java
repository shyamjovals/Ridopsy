package com.example.prasanna.prot1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class mapslog extends AppCompatActivity {
    public String curuser, lati, longi, tid;
    Button trans,trans2;
    public Boolean flag=true,flag2=true;
    public Schedule doc;
    public ProgressDialog progressDialog;
    public Double mylat, mylong;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;
    public final long MIN_TIME_BW_UPDATES = 3000;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapslog);

        curuser = this.getIntent().getExtras().getString("currentuser");
        tid=this.getIntent().getExtras().getString("ridetype");;
        Address address = null;

        //    doc = (Schedule) this.getIntent().getSerializableExtra("Object");
        //    tid = doc.username;
        System.out.println("Obj is:"+tid+"and user is"+curuser);
        trans = (Button) findViewById(R.id.transmit);
        trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gps = new GPSTracker(mapslog.this);
                if (gps.canGetLocation()) {

                    mylat = gps.getLatitude();
                    mylong = gps.getLongitude();
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + mylat + "\nLong: " + mylong, Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }
                lati = mylat.toString();
                longi = mylong.toString();
                ////
                new MyTask().execute();
                // flag = true;
            }
        });
        trans2 = (Button) findViewById(R.id.stoptrans);
        trans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=false;
                Toast.makeText(getApplicationContext(), "Transmission Stopped", Toast.LENGTH_LONG).show();


            }
        });

    }
    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //         progressDialog = ProgressDialog.show(mapslog.this, "Message", "Updating Location");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

            // get the string from params, which is an array
            try {
                CloudantClient client = ClientBuilder.account("bbfb0d3f-4e56-48c1-9cf0-2d43cdc5858b-bluemix")
                        .username("bbfb0d3f-4e56-48c1-9cf0-2d43cdc5858b-bluemix")
                        .password("4f9028bb80ca5cd704330f99a4c353f24912cf5918c74b718475ab37254f96f7")
                        .build();

                Database db = client.database("pickmybus", false);
                doc=db.find(Schedule.class,curuser);
                doc.latit=mylat;
                doc.longit=mylong;
                db.update(doc);
                return "SUCCESS";

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Network Busy!!", Toast.LENGTH_LONG).show();
                //System.out.println("Error SMS "+e);

            }


            return "FAILED";
        }



        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("SUCCESS")){
                System.out.println("SUCCESS");
            }
            else{
                Toast.makeText(getApplicationContext(),"Failed..",Toast.LENGTH_LONG).show();
            }
            if(flag) {
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        trans.performClick();
                    }
                }.start();
            }
               // trans.performClick();

            //   progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }
}
