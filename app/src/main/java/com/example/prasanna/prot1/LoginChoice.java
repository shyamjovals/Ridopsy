package com.example.prasanna.prot1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginChoice extends AppCompatActivity {

    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);
        b1=(Button)findViewById(R.id.bUser);
        b2=(Button)findViewById(R.id.bDriver);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(LoginChoice.this,LoginActivityUser.class);
                startActivity(n);
            }
        });
        
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(LoginChoice.this,LoginActivityDriver.class);
                startActivity(n);
            }
        });
    }
}
