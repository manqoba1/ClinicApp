package com.example.sifiso.clinicapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sifiso.cbslibrary.MainPagerActivity;
import com.example.sifiso.cbslibrary.models.PatientDTO;
import com.example.sifiso.cbslibrary.util.DataUtil;
import com.example.sifiso.cbslibrary.util.EmailValidator;
import com.example.sifiso.cbslibrary.util.SharedUtil;
import com.example.sifiso.cbslibrary.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity {

    Context ctx;
    Activity activity;
    Button bsRegister, bsSignin, rsRegister;
    EditText esPin, rsTeamName, rsMemberName, rsMemberSurname;
    EditText rsCellphone, rsPin;

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
        getMenuInflater().inflate(R.menu.menu_register, menu);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
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

        if (rsMemberName.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Enter Patient Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rsMemberSurname.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Enter Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rsMemberEmail.getText().toString().isEmpty() /*|| validator.validate(rsMemberEmail.getText().toString())*/) {
            Toast.makeText(ctx, "Enter Enail Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rsPin.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "invalid pin", Toast.LENGTH_SHORT).show();
            return;
        }
        PatientDTO dto = new PatientDTO();
        dto.setEmail(rsMemberEmail.getText().toString());
        dto.setFirstName(rsMemberName.getText().toString());
        dto.setLastName(rsMemberSurname.getText().toString());
        dto.setPhoneNumber(rsCellphone.getText().toString());
        dto.setPassword(rsPin.getText().toString());
        DataUtil.registerPatient(ctx, dto, new DataUtil.DataUtilInterface() {
            @Override
            public void onResponse(JSONObject r) {
                try {
                    if (r.getInt("success") == 0) {
                        Util.showErrorToast(ctx, r.getString("message"));
                        return;
                    }
                    Log.d(LOG, r.toString());
                    patient = patientData(r.getJSONArray("patient"));
                    SharedUtil.savePatientr(ctx, patient);
                    Intent intent = new Intent(RegisterActivity.this, MainPagerActivity.class);
                    intent.putExtra("patient", patient);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    PatientDTO patient;

    private PatientDTO patientData(JSONArray ar) {
        PatientDTO dto = new PatientDTO();
        try {
            JSONObject ob = ar.getJSONObject(0);
            Log.d(LOG, ob.toString());
            dto.setEmail(ob.getString("email"));
            dto.setPhoneNumber(ob.getString("phoneNumber"));
            dto.setPhoneNumber(ob.getString("phoneNumber"));
            dto.setFirstName(ob.getString("firstName"));
            dto.setMiddleName(ob.getString("middleName"));
            dto.setLastName(ob.getString("lastName"));
            dto.setPassword(ob.getString("password"));
            dto.setPatientID(ob.getInt("patientID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dto;
    }

    private EmailValidator validator;

    public void setFields() {
        validator = new EmailValidator();
        rsRegister = (Button) findViewById(R.id.btnReg);
        rsMemberName = (EditText) findViewById(R.id.edtMemberName);
        rsMemberSurname = (EditText) findViewById(R.id.edtMemberLastNAme);
        rsMemberEmail = (AutoCompleteTextView) findViewById(R.id.edtMemberEmail);
        rsCellphone = (EditText) findViewById(R.id.edtMemberPhone);
        rsPin = (EditText) findViewById(R.id.edtMemberPassword);
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
              ArrayAdapter adapter = new ArrayAdapter<String>(ctx, R.layout.xsimple_spinner_item, emailAccountList);


             rsMemberEmail.setAdapter(adapter);
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
