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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends ActionBarActivity {

    Context ctx;
    Activity activity;
    Button bsRegister, bsSignin, rsRegister, tsNext;
    EditText esPin, rsTeamName, rsMemberName, rsMemberSurname;
    EditText rsCellphone, rsPin;
    EditText rsTown;
    ViewStub viewStub;
    View regMemberLayout,/* SignLayout, regMiddlelay,*/
            regTeamLayout;
    ProgressBar reg_progress;
    String email, strTown;
    AutoCompleteTextView rsMemberEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = this;
        ctx = getApplicationContext();
        getSupportActionBar().setTitle("Register Patient");
        setFields();
        getEmail();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu); getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register Patient");
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


    public void sendRegistration() {

        if (rsTeamName.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Enter Patient Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rsTown == null) {

            Toast.makeText(ctx, "Select Towmn", Toast.LENGTH_SHORT).show();
            return;
        }


        if (rsMemberSurname.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Enter Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rsMemberEmail.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Enter Enail Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rsPin.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "invalid pin", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    public void setFields() {
        rsRegister = (Button) findViewById(R.id.btnReg);
        rsMemberName = (EditText) findViewById(R.id.edtMemberName);
        rsMemberSurname = (EditText) findViewById(R.id.edtMemberLastNAme);
        rsMemberEmail = (AutoCompleteTextView) findViewById(R.id.edtMemberEmail);

        rsRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegistration();
            }
        });

    }

    List<String> emailAccountList;

    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am.getAccounts();
        if (accts.length == 0) {
            Toast.makeText(ctx, "No Accounts found. Please create one and try again", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        emailAccountList = new ArrayList<String>();
        if (accts != null) {
            for (int i = 0; i < accts.length; i++) {
                emailAccountList.add(accts[i].name);
            }
          //  ArrayAdapter adapter = new ArrayAdapter<String>(ctx, R.layout.xsimple_spinner_item, emailAccountList);


           // rsMemberEmail.setAdapter(adapter);
            rsMemberEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    email = emailAccountList.get(position);
                }
            });
        }

    }


    static final String LOG = "RegisterActivity";



}
