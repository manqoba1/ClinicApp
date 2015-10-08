package com.example.sifiso.clinicapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sifiso.cbslibrary.MainPagerActivity;
import com.example.sifiso.cbslibrary.models.BookingDTO;
import com.example.sifiso.cbslibrary.models.ClinicDTO;
import com.example.sifiso.cbslibrary.models.GcmDeviceDTO;
import com.example.sifiso.cbslibrary.models.PatientDTO;
import com.example.sifiso.cbslibrary.models.ResponsePojo;
import com.example.sifiso.cbslibrary.models.TownDTO;
import com.example.sifiso.cbslibrary.util.DataUtil;
import com.example.sifiso.cbslibrary.util.GCMUtil;
import com.example.sifiso.cbslibrary.util.SharedUtil;
import com.example.sifiso.cbslibrary.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    Context ctx;
    Button bsFacebook, bsSignin, bsTwitter, bsGoogle;
    EditText esPin;
    String email;
    TextView sign_up;
    AutoCompleteTextView esEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = getApplicationContext();

        setFields();
        getEmail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Patient sign in");
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

    public void setFields() {


        bsSignin = (Button) findViewById(R.id.btnLogSubmit);
        esEmail = (AutoCompleteTextView) findViewById(R.id.edtLogEmail);
        esPin = (EditText) findViewById(R.id.edtLogPassword);
        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        checkVirgin();

        bsSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignIn();

            }
        });


    }

    public void sendSignIn() {

        if (esEmail.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (esPin.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Invalid Pin", Toast.LENGTH_SHORT).show();
            return;
        }

        DataUtil.login(ctx, esEmail.getText().toString(), esPin.getText().toString(), gcmDevice, new DataUtil.DataUtilInterface() {
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
                    Intent intent = new Intent(LoginActivity.this, MainPagerActivity.class);
                    intent.putExtra("patient", patient);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(LOG, error);
            }

        });
    }

    private PatientDTO patient;
    GcmDeviceDTO gcmDevice;

    private void registerGCMDevice() {
        boolean ok = checkPlayServices();

        if (ok) {
            Log.e(LOG, "############# Starting Google Cloud Messaging registration");
            GCMUtil.startGCMRegistration(getApplicationContext(), new GCMUtil.GCMUtilListener() {
                @Override
                public void onDeviceRegistered(String id) {
                    Log.e(LOG, "############# GCM - we cool, cool.....: " + id);
                    SharedUtil.storeRegistrationId(ctx, id);
                    gcmDevice = new GcmDeviceDTO();
                    gcmDevice.setManufacturer(Build.MANUFACTURER);
                    gcmDevice.setModel(Build.MODEL);
                    gcmDevice.setSerialNumber(Build.SERIAL);
                    gcmDevice.setProduct(Build.PRODUCT);
                    gcmDevice.setAndroidVersion(Build.VERSION.RELEASE);
                    gcmDevice.setRegistrationID(id);

                }

                @Override
                public void onGCMError() {
                    Log.e(LOG, "############# onGCMError --- we got GCM problems");

                }
            });
        }
    }

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

    private List<ClinicDTO> clinicList(JSONArray ar) throws JSONException {
        List<ClinicDTO> list = new ArrayList<>();
        for (int i = 0; i < ar.length(); i++) {
            JSONObject o = ar.getJSONObject(i);
            ClinicDTO dto = new ClinicDTO();
            dto.setClinicID(o.getInt("clinicID"));
            dto.setPhoneNumber(o.getString("phoneNumber"));
            dto.setEmail(o.getString("email"));
            dto.setLatitude(o.getDouble("latitude"));
            dto.setLongitude(o.getDouble("longitude"));
            list.add(dto);
        }
        return list;
    }

    private void checkVirgin() {
        //SharedUtil.clearTeam(ctx);
        PatientDTO dto = SharedUtil.getPatient(ctx);
        if (dto != null) {
            Log.i(LOG, "++++++++ Not a virgin anymore ...checking GCM registration....");
            String id = SharedUtil.getRegistrationId(getApplicationContext());
            if (id == null) {
                registerGCMDevice();
            }

            Intent intent = new Intent(ctx, MainPagerActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        registerGCMDevice();
    }

    public boolean checkPlayServices() {
        Log.w(LOG, "checking GooglePlayServices .................");
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                //         PLAY_SERVICES_RESOLUTION_REQUEST).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));
                return false;
            } else {
                Log.i(LOG, "This device is not supported.");
                throw new UnsupportedOperationException("GooglePlayServicesUtil resultCode: " + resultCode);
            }
        }
        return true;
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

            esEmail.setAdapter(adapter);
            esEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    email = emailAccountList.get(position);
                    esEmail.setText(email);
                }
            });


        }

    }


    static final String LOG = "LoginActivity";


}
