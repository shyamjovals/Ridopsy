package com.example.prasanna.prot1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public Thread thread;
    private GoogleMap mMap;
    Button checkloc;
    Marker markerx;
    public String setno;
    public Schedule docs;
    public Boolean flagt=true;
    int mm=1,hh;
    public int count, p = -3, h = 1, g, top = 0, tmpcnt = 3, forbid = 99,c=0;
    public Double smallest;
    public int key;
    public String tid,location9,newlat = null, newlong = null;
    public String elim[]=new String[100];
    public int az=0;
    public String tmp3;
    public ProgressDialog progressDialog;
    public String serverKey = "AIzaSyDrtPkNy1R_lIkP0_LPVnh2izG-u1kfW3k";
    public Double latx, longx, mylat, mylong;
    public Double curlat, curlong,curlat1,curlong1;
    public boolean flag1 = true, flag2 = true, flag3 = true, checkd1 = true, checkd2 = true, checkd3 = true,flagz=true;
    Double[] distarr = new Double[100];
    String[] durarr = new String[100];
    Double[][] drivelist = new Double[1000][1000];
    LatLng tmp;
    List<Schedule>samp=new ArrayList<>();
    List<Marker>markerss=new ArrayList<>();
    Boolean []flagq=new Boolean[100];
    Double finaldist, testdist;
    String busname[]=new String[100];
    String finalduration, testtime;
    TextView txt;
    String resp;
    Route route;
    Leg leg;
    Marker mMarker0, mMarker1, mMarker2;
    //String busno6,busdep6,busdest6,busno7,busdep7,busdest7,busno8,busdep8,busdest8;
    List<String>busno6=new ArrayList<>();
    List<String>busdep6=new ArrayList<>();
    List<String>busdest6=new ArrayList<>();
    LatLng mylatlang, stoplatlang;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Address address = null;
        //  docs = (Schedule)this.getIntent().getSerializableExtra("Object");
       location9 = this.getIntent().getExtras().getString("stop");
        //tid=docs.travel;

        gps=new GPSTracker(MapsActivity.this);
        if(gps.canGetLocation()){

            curlat = gps.getLatitude();
            curlong = gps.getLongitude();

        }

    //    curlat=11.917954;
     //   curlong=79.639989;

        mylatlang = new LatLng(curlat, curlong);

        List<Address> addressList = null;
        if (location9 != null || !location9.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location9, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        }

        mylat = address.getLatitude();
        mylong = address.getLongitude();
        stoplatlang = new LatLng(mylat, mylong);
        System.out.println("From geocoder"+mylat+" "+mylong);
        GoogleDirection.withServerKey(serverKey)
                .from(mylatlang)
                .to(stoplatlang)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction) {
                        // Do something here
                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {

                        }
                        if (status.equals(RequestResult.NOT_FOUND)) {
                            // Do something

                        }
                        try {
                            route = direction.getRouteList().get(0);
                            leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String tmp1 = distanceInfo.getText();
                            tmp1 = tmp1.substring(0, tmp1.length() - 3);
                            String tmp2 = durationInfo.getText();
                            testdist = Double.parseDouble(tmp1);
                            testtime = tmp2;
                            System.out.println("Housetostop" + testdist + " " + testtime);

                        } catch (Exception e) {
                            System.out.println("Exiting:" + e);
                        }

                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();


                        PolygonOptions rectOptions = new PolygonOptions();


                        for (int i = 0; i < directionPositionList.size() - 1; i++) {
                            LatLng first = directionPositionList.get(i);
                            LatLng second = directionPositionList.get(i + 1);
                            Polyline line1 = mMap.addPolyline(new PolylineOptions()
                                    .add(first, second)
                                    .width(5)
                                    .color(Color.MAGENTA));
                            // System.out.println(first+","+second);
                        }


                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });

        checkloc = (Button) findViewById(R.id.check);
        checkloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gps=new GPSTracker(MapsActivity.this);
                if(gps.canGetLocation()){

                    curlat1 = gps.getLatitude();
                    curlong1 = gps.getLongitude();

                }
                LatLng uppos=new LatLng(curlat1,curlong1);
                markerx.remove();
                markerx=mMap.addMarker(new MarkerOptions().position(uppos).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Me"));
                new MyTask().execute();

            }
        });

}


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(stoplatlang).title(location9));
        markerx=mMap.addMarker(new MarkerOptions().position(mylatlang).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Me"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stoplatlang));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stoplatlang, 12.0f));
    }
    public void findleast(){

        try {
            for (int i = c; i < hh; i++) {
                for(int j=c;j<hh-1;j++)
                {
                    if(distarr[j]>distarr[j+1])
                    {
                        double tmp=distarr[j];
                        distarr[j]=distarr[j+1];
                        distarr[j+1]=tmp;

                        String tmp2=durarr[j];
                        durarr[j]=durarr[j+1];
                        durarr[j+1]=tmp2;

                        tmp3=busname[j];
                        busname[j]=busname[j+1];
                        busname[j+1]=tmp3;
                    }
                }
                }

            if(finalduration.equals(testtime))
            {
                elim[az++]=tmp3;
                c++;
            }
            for(int j=0;j<hh;j++)
            {
                for(int gg=0;gg<az;gg++)
                {
                    if(tmp3==elim[gg])
                    {
                        flagt=false;
                    }
                }
                if(flagt)
                {
                    finaldist=distarr[j];
                    finalduration=durarr[j];
                    break;
                }
            }
            flagt=true;
          //  finaldist=distarr[0];
          //  finalduration=durarr[0];
                txt = (TextView) findViewById(R.id.details);
                String abc = "Nearest Ride "+busname[0]+" is "+finaldist.toString()+" Kms away" + "\nTime to reach your Stop:" + finalduration;
                txt.setText(abc);
            }
        catch(Exception e)
        {
            System.out.println("qqq"+e);
        }
    }

private class MyTask extends AsyncTask<String, Integer, String> {

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // progressDialog = ProgressDialog.show(MapsActivity.this, "Message", "Loading");

    }

    // This is run in a background thread
    @Override
    protected String doInBackground(String... params) {
        // get the string from params, which is an array
        try {
            System.out.println("Inside try");
            CloudantClient client = ClientBuilder.account("bbfb0d3f-4e56-48c1-9cf0-2d43cdc5858b-bluemix")
                    .username("bbfb0d3f-4e56-48c1-9cf0-2d43cdc5858b-bluemix")
                    .password("4f9028bb80ca5cd704330f99a4c353f24912cf5918c74b718475ab37254f96f7")
                    .build();
            Database db = client.database("pickmybus", false);
           // docs=db.find(Schedule.class,"transport501");

            samp= db.findByIndex("{\"selector\":{\"ride\":\"" + "Transport" + "\"}}", Schedule.class);
            hh=samp.size();
            //System.out.println("Finally:"+samp+"of"+hh);
            for(int i=0;i<hh;i++)
            {
                drivelist[i][1]=samp.get(i).latit;
                drivelist[i][2]=samp.get(i).longit;
                busno6.add(samp.get(i).busno);
                busdep6.add(samp.get(i).busdep);
                busdest6.add(samp.get(i).busdest);
                busname[i]=samp.get(i).busno;
            }

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
        if (result.equals("SUCCESS")) {
            System.out.println("SUCCESS");

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    LinearLayout info = new LinearLayout(MapsActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(MapsActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(MapsActivity.this);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
            for(int i=0;i<hh;i++){
                final int u=i;
                LatLng origin = new LatLng(drivelist[i][1], drivelist[i][2]);
                LatLng destination = new LatLng(mylat, mylong);
            if (top != 0) {
                markerss.get(i).remove();
            }
            markerss.add(i,mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Ride " + busno6.get(u)).snippet("Departure: " + busdep6.get(u) + "\nDestination: " + busdest6.get(u))));
                GoogleDirection.withServerKey(serverKey)
                        .from(origin)
                        .to(destination)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction) {
                                // Do something here
                                String status = direction.getStatus();
                                if (status.equals(RequestResult.OK)) {

                                }
                                if (status.equals(RequestResult.NOT_FOUND)) {
                                    // Do something

                                }
                                try {
                                    route = direction.getRouteList().get(0);
                                    leg = route.getLegList().get(0);
                                    Info distanceInfo = leg.getDistance();
                                    Info durationInfo = leg.getDuration();
                                    String tmp1 = distanceInfo.getText();
                                    tmp1 = tmp1.substring(0, tmp1.length() - 3);
                                    String tmp2 = durationInfo.getText();
                                    distarr[u] = Double.parseDouble(tmp1);
                                    durarr[u] = tmp2;
                                    //System.out.println(0 + "Impo" + String.valueOf(distarr[i]) + " :" + durarr[i]);
                                } catch (Exception e) {
                                    System.out.println("Exiting:" + e);
                                }


                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                System.out.println("Direction 0 failed");
                            }
                        });
        }
            top=1;
            findleast();

        } else {
            Toast.makeText(getApplicationContext(), "Failed..", Toast.LENGTH_LONG).show();
        }
        if(c<hh)
        {
            new CountDownTimer(2000,1000)
            {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    checkloc.performClick();
                }
            }.start();
        }


    }

}

}
