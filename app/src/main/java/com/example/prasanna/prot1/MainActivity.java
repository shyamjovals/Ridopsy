package com.example.prasanna.prot1;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onBackPressed() {

        android.os.Process.killProcess(android.os.Process.myPid());
        // This above line close correctly
    }
    public void divein(View v)
    {
        if(v.getId()==R.id.divein)
        {
            Intent i=new Intent(MainActivity.this,LoginChoice.class);
            startActivity(i);
        }
    }
}
