package com.example.prasanna.prot1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

public class Transreg extends AppCompatActivity {

    public String user5,pass5,ride5;
    Button regi;
    public ProgressDialog progressDialog;
    EditText busno,busdep,busdest;
    String busno1,busdep1,busdest1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transreg);
        user5 = this.getIntent().getExtras().getString("username");
        pass5 = this.getIntent().getExtras().getString("password");
        ride5 = this.getIntent().getExtras().getString("ridetype");

        busno=(EditText)findViewById(R.id.tfbus);
        busdep=(EditText)findViewById(R.id.tfdep);
        busdest=(EditText)findViewById(R.id.tfdest);
        regi=(Button)findViewById(R.id.breg);
        regi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                busno1=busno.getText().toString();
                busdep1=busdep.getText().toString();
                busdest1=busdest.getText().toString();
                new MyTask().execute();

            }
        });
    }
    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Transreg.this, "Message", "Creating Account...");

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

                db.save(new Schedule(user5,pass5,ride5,busno1,busdep1,busdest1,"","","","","","","","","","","","",0.0,0.0));

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
                Intent n = new Intent(Transreg.this,LoginActivityDriver.class);
                startActivity(n);
            }
            else{
                Toast.makeText(getApplicationContext(),"Failed..",Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }
}
