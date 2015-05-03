package com.example.sifiso.clinicapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class BookingActivity extends ActionBarActivity {
    Context ctx;
    Activity activity;
    Button btn_someone,btn_me_book;
    TextView Sp_clinic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        activity = this;
        ctx = getApplicationContext();
        getSupportActionBar().setTitle("Register Patient");
        setFields();

    }

    public void setFields() {
        btn_someone = (Button) findViewById(R.id.btn_someone);
        btn_me_book = (Button) findViewById(R.id.btn_me_book);
        Sp_clinic = (TextView) findViewById(R.id.Sp_clinic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_booking, menu);
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

    public void sendBooking() {


    }


    List<String> emailAccountList;


    static final String LOG = "BookingActivity";


}
