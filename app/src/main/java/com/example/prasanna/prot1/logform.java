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

public class logform extends AppCompatActivity {
    EditText email,password,stop,travel;
    Button register;
    public ProgressDialog progressDialog;
    String email1,password1,stop1,travel1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logform);


        email=(EditText)findViewById(R.id.usn);
        password=(EditText)findViewById(R.id.pass);
        stop=(EditText)findViewById(R.id.tfstop);

        System.out.println("email id is:"+ email1);
        register=(Button)findViewById(R.id.createac);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email1=email.getText().toString();
                password1=password.getText().toString();
                stop1=stop.getText().toString();
                new MyTask4().execute(email1,password1,stop1);
            }
        });
    }

    private class MyTask4 extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(logform.this, "Message", "Creating Account...");

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
                System.out.println(email1+" "+password1+" "+stop1+" "+travel1);
                Database db = client.database("pickmybus", false);
                db.save(new Schedule(email1,password1,"","","","","","","","","","","","","","","",stop1,0.0,0.0));
                //db.save(new Schedule(email1,password1,stop1,travel1,0.0,0.0));

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
                Intent n = new Intent(logform.this,LoginActivityUser.class);

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
