package com.example.sifiso.cbslibrary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sifiso.cbslibrary.adapter.AppointmentAdapter;
import com.example.sifiso.cbslibrary.fragment.AppointmentListFragment;
import com.example.sifiso.cbslibrary.fragment.PageFragment;
import com.example.sifiso.cbslibrary.models.BookingDTO;
import com.example.sifiso.cbslibrary.models.PatientDTO;
import com.example.sifiso.cbslibrary.models.ResponsePojo;
import com.example.sifiso.cbslibrary.util.DataUtil;
import com.example.sifiso.cbslibrary.util.SharedUtil;
import com.example.sifiso.cbslibrary.util.ZoomOutPageTransformerImpl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainPagerActivity extends ActionBarActivity {
    static final String LOG = MainPagerActivity.class.getSimpleName();
    Context ctx;
    ProgressBar progressBar;
    AppointmentListFragment appointmentListFragment;
    List<PageFragment> pageFragmentList;
    ViewPager mPager;
    Menu mMenu;
    PagerAdapter adapter;
    private ResponsePojo response;
    private TextView RL_add;
    private PatientDTO patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        patient = SharedUtil.getPatient(ctx);
        setField();
    }

    private void setField() {
        mPager = (ViewPager) findViewById(R.id.SITE_pager);
        RL_add = (TextView) findViewById(R.id.RL_add);
        RL_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // mPager.setOffscreenPageLimit(NUM_ITEMS - 1);

        PagerTitleStrip strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        strip.setVisibility(View.GONE);
    }


    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        appointmentListFragment = new AppointmentListFragment();
        Bundle data = new Bundle();
        data.putSerializable("response", response);
        data.putSerializable("bookings", (java.io.Serializable) response.getBookingList());

        appointmentListFragment.setArguments(data);


        pageFragmentList.add(appointmentListFragment);
        // pageFragmentList.add(evaluationListFragment);

        initializeAdapter();


    }

    private void initializeAdapter() {
        try {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(adapter);
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int arg0) {
                    PageFragment pf = pageFragmentList.get(arg0);


                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
            ZoomOutPageTransformerImpl z = new ZoomOutPageTransformerImpl();
            mPager.setPageTransformer(true, z);
        } catch (Exception e) {
            Log.e(LOG, "-- Some shit happened, probably IllegalState of some kind ...",e);
        }
    }

    private void getDashboardData() {
        DataUtil.dashData(ctx, patient.getPatientID(), new DataUtil.DataUtilInterface() {
            @Override
            public void onResponse(JSONObject r) {
                DataUtil.getData(ctx, r, new DataUtil.JsonifyListener() {
                    @Override
                    public void onResponseJSon(ResponsePojo resp) {
                        if (resp.getSuccess() == 0) {
                            return;
                        }
                        response = resp;
                        buildPages();

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        });
    }



    private class PagerAdapter extends FragmentStatePagerAdapter implements PageFragment {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Title";

            switch (position) {
                case 0:

                    break;


                default:
                    break;
            }
            return title;
        }

        @Override
        public void animateCounts() {

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getDashboardData();
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
}
