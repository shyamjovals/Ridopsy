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

public class logindriver extends AppCompatActivity {
    public Schedule doc;
    String tid;
    public ProgressDialog progressDialog;
    EditText email,password;
    Button emailSigninButton;
    Button Signup;
    public String given_uname,given_pass,email1,pass1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logindriver);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);

        emailSigninButton=(Button)findViewById(R.id.email_sign_in_button);
        emailSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email1=email.getText().toString();
                pass1=password.getText().toString();
                new MyTask3().execute(email1,pass1);
            }
        });
        Signup=(Button)findViewById(R.id.bsignup);
        Signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(logindriver.this,logform.class);
                startActivity(i);
            }
        });
    }

    private class MyTask3 extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(logindriver.this, "Message", "Logging In...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            given_uname = params[0];
            given_pass = params[1];
            String n = given_uname;
            String p = given_pass;
            System.out.println(n);
            System.out.println(p);
            try {
                CloudantClient client = ClientBuilder.account("bbfb0d3f-4e56-48c1-9cf0-2d43cdc5858b-bluemix")
                        .username("bbfb0d3f-4e56-48c1-9cf0-2d43cdc5858b-bluemix")
                        .password("4f9028bb80ca5cd704330f99a4c353f24912cf5918c74b718475ab37254f96f7")
                        .build();

                Database db = client.database("pickmybus", false);
                doc = db.find(Schedule.class, given_uname);

                if (doc.password.equals(given_pass)) {
                    tid=doc.ride;
                    return "SUCCESS";
                }
            } catch (Exception e) {
                return e.toString();
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
                Intent n = new Intent(logindriver.this,mapslog.class);
                n.putExtra("currentuser",given_uname);
                n.putExtra("ridetype",tid);
                startActivity(n);
            }
            else{
                Toast.makeText(getApplicationContext(),"Wrong Username or Password..",Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }
}

