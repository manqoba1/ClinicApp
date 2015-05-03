package com.example.sifiso.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;


public class LandPageActivity extends ActionBarActivity {
    ImageView imageView;
    TextView imageText;
    Timer timer;
    Context ctx;
    Button bLogin,bReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_page);
        getSupportActionBar().hide();
        ctx = getApplicationContext();


        imageView = (ImageView) findViewById(R.id.welcomeimg);
        imageView = (ImageView) findViewById(R.id.PSN_imagex);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandPageActivity.this,LoginActivity.class);
                startActivity(intent);
                getSupportActionBar().setTitle("Sign in");
            }
        });


        bReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandPageActivity.this, RegisterActivity.class);
                startActivity(intent);
                getSupportActionBar().setTitle("Register Patient");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_land_page, menu);
        startActivity(new Intent(LandPageActivity.this, LandPageActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    static final String LOG = LandPageActivity.class.getSimpleName();
}
