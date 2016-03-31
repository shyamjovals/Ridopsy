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

public class Mapsact4 extends FragmentActivity implements OnMapReadyCallback {

    public Thread thread;
    private GoogleMap mMap;
    Button checkloc;
    Marker markerx,markerx2;
    public Schedule docs;
    List<Marker>markerss=new ArrayList<>();
    List<Schedule>samp=new ArrayList<>();
    public int hh;
    public int count, p = -3, h = 1, g, top = 0, tmpcnt = 3, forbid = 99;
    public Double smallest;
    public int key;
    public String tid,newlat = null, newlong = null;
    public ProgressDialog progressDialog;
    public String serverKey = "AIzaSyDrtPkNy1R_lIkP0_LPVnh2izG-u1kfW3k";
    public Double latx, longx, mylat, mylong;
    public Double curlat, curlong,curlat1,curlong1;
   // public String bankname6,haltven6,haltdur6,bankname7,haltven7,haltdur7,bankname8,haltven8,haltdur8;
    List<String>bankname6=new ArrayList<>();
    List<String>haltven6=new ArrayList<>();
    List<String>haltdur6=new ArrayList<>();
    public String fnames[]=new String[100];
    public boolean flag1 = true, flag2 = true, flag3 = true, checkd1 = true, checkd2 = true, checkd3 = true,flagz=true;
    Double[] distarr = new Double[100];
    String[] durarr = new String[100];
    Double[][] drivelist = new Double[1000][1000];
    LatLng tmp;
    Double finaldist, testdist;
    String finalduration, testtime;
    TextView txt;
    String resp;
    Route route;
    Leg leg;
    Marker mMarker0, mMarker1, mMarker2;
    LatLng mylatlang, stoplatlang,bbb;
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
        //tid=docs.travel;

        gps=new GPSTracker(Mapsact4.this);
        if(gps.canGetLocation()){

            curlat = gps.getLatitude();
            curlong = gps.getLongitude();

        }

        mylatlang = new LatLng(curlat, curlong);

        checkloc = (Button) findViewById(R.id.check);
        checkloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gps = new GPSTracker(Mapsact4.this);
                if (gps.canGetLocation()) {

                    curlat1 = gps.getLatitude();
                    curlong1 = gps.getLongitude();

                }
                LatLng uppos = new LatLng(curlat1, curlong1);
                markerx.remove();
                markerx = mMap.addMarker(new MarkerOptions().position(uppos).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Me"));
                if(flagz)
                {
                    curlat=curlat1;
                    curlong=curlong1;
                }
                new MyTask().execute();

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                flagz=false;
                bbb=markerx2.getPosition();
                curlat=bbb.latitude;
                curlong=bbb.longitude;

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                markerx2.remove();
                markerx2=mMap.addMarker(new MarkerOptions().position(bbb).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title("Drag and Drop").draggable(true));
            }
        });
        LatLng tmpl=new LatLng(mylatlang.latitude+0.01,mylatlang.longitude+0.01);
        markerx=mMap.addMarker(new MarkerOptions().position(mylatlang).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Me"));
        markerx2=mMap.addMarker(new MarkerOptions().position(tmpl).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).title("Drag and Drop").draggable(true));mMap.moveCamera(CameraUpdateFactory.newLatLng(mylatlang));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylatlang, 12.0f));
    }
    public void findleast(){


        try {
            for (int i = 0; i < hh; i++) {
                for(int j=0;j<hh-1;j++)
                {
                    if(distarr[j]>distarr[j+1])
                    {
                        double tmp=distarr[j];
                        distarr[j]=distarr[j+1];
                        distarr[j+1]=tmp;

                        String tmp2=durarr[j];
                        durarr[j]=durarr[j+1];
                        durarr[j+1]=tmp2;

                        String tmp3=fnames[j];
                        fnames[j]=fnames[j+1];
                        fnames[j+1]=tmp3;
                    }
                }
            }
            finaldist = distarr[0];
            finalduration = durarr[0];

            txt = (TextView) findViewById(R.id.details);
            String abc = "Nearest is Atm "+fnames[0]+": "+finaldist.toString() + " Kms away\nReaches you in " + finalduration;
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

                samp= db.findByIndex("{\"selector\":{\"ride\":\"" + "Mobile ATMs" + "\"}}", Schedule.class);
                hh=samp.size();
                //System.out.println("Finally:"+samp+"of"+hh);
                for(int i=0;i<hh;i++)
                {
                    drivelist[i][1]=samp.get(i).latit;
                    drivelist[i][2]=samp.get(i).longit;
                    bankname6.add(samp.get(i).bankname);
                    haltven6.add(samp.get(i).haltven);
                    haltdur6.add(samp.get(i).haltdur);
                    fnames[i]=samp.get(i).bankname;
                }

                return "SUCCESS";

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Network Busy!!", Toast.LENGTH_LONG).show();
                //System.out.println("Error SMS "+e);

            }


            return "SUCCESS";
            //  return "FAILED";
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
                        LinearLayout info = new LinearLayout(Mapsact4.this);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(Mapsact4.this);
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(Mapsact4.this);
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
                    LatLng destination = new LatLng(curlat, curlong);
                    if (top != 0) {
                        markerss.get(i).remove();
                    }
                    markerss.add(i,mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(bankname6.get(u)).snippet("Halt Venue: "+haltven6.get(u)+"\nHalt Duration: "+haltdur6.get(u))));
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
            if(flag1||flag2||flag3)
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
