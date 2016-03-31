package com.example.prasanna.prot1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

public class logformdriver extends AppCompatActivity {
    EditText email,password,stop,travel;
    Button conti;
    public char x;
    public int r;
    public String tmp,ridechoice;
    public ProgressDialog progressDialog;
    String email1,password1;
    RadioGroup radioGroup;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logformdriver);


        email=(EditText)findViewById(R.id.usn);
        password=(EditText)findViewById(R.id.pass);
        radioGroup=(RadioGroup)findViewById(R.id.rgroup);
        System.out.println("email id is:"+ email1);
        conti=(Button)findViewById(R.id.bcont);
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email1 = email.getText().toString();
                password1 = password.getText().toString();
                radioButton=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                ridechoice=radioButton.getText().toString();
                System.out.println("Choice:" + ridechoice);
                if(ridechoice.equals("Transport"))
                {
                    Intent i=new Intent(logformdriver.this,Transreg.class);
                    i.putExtra("username",email1);
                    i.putExtra("password",password1);
                    i.putExtra("ridetype",ridechoice);
                    startActivity(i);
                }
                if(ridechoice.equals("My Friends"))
                {
                    Intent i=new Intent(logformdriver.this,Friendsreg.class);
                    i.putExtra("username",email1);
                    i.putExtra("password",password1);
                    i.putExtra("ridetype",ridechoice);
                    startActivity(i);
                }
                if(ridechoice.equals("Ambulances"))
                {
                    Intent i=new Intent(logformdriver.this,Ambureg.class);
                    i.putExtra("username",email1);
                    i.putExtra("password",password1);
                    i.putExtra("ridetype",ridechoice);
                    startActivity(i);
                }
                if(ridechoice.equals("Mobile ATMs"))
                {
                    Intent i=new Intent(logformdriver.this,Atmreg.class);
                    i.putExtra("username",email1);
                    i.putExtra("password",password1);
                    i.putExtra("ridetype",ridechoice);
                    startActivity(i);
                }
                if(ridechoice.equals("Cabs"))
                {
                    Intent i=new Intent(logformdriver.this,Cabsreg.class);
                    i.putExtra("username",email1);
                    i.putExtra("password",password1);
                    i.putExtra("ridetype",ridechoice);
                    startActivity(i);
                }

            }
        });
    }

}
