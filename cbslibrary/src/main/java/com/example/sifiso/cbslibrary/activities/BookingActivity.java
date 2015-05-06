package com.example.sifiso.cbslibrary.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.sifiso.cbslibrary.R;
import com.example.sifiso.cbslibrary.fragment.BookingFragment;
import com.example.sifiso.cbslibrary.fragment.PageFragment;
import com.example.sifiso.cbslibrary.fragment.SearchClinicFragment;
import com.example.sifiso.cbslibrary.models.ClinicDTO;
import com.example.sifiso.cbslibrary.models.ResponsePojo;
import com.example.sifiso.cbslibrary.util.ZoomOutPageTransformerImpl;

import java.util.ArrayList;
import java.util.List;


public class BookingActivity extends ActionBarActivity implements SearchClinicFragment.SearchClinicFragmentListener, BookingFragment.BookingFragmentListener {
    Context ctx;
    Activity activity;
    List<PageFragment> pageFragmentList;
    BookingFragment bookingFragment;
    SearchClinicFragment searchClinicFragment;
    ViewPager mPager;
    Menu mMenu;
    PagerAdapter adapter;
    ResponsePojo response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mPager = (ViewPager) findViewById(R.id.SITE_pager);
        getSupportActionBar().setTitle("Make an appointment");
        PagerTitleStrip strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);

        ctx = getApplicationContext();
        if (savedInstanceState != null) {
            response = (ResponsePojo) savedInstanceState.getSerializable("response");
            mClinic = (ClinicDTO) savedInstanceState.getSerializable("response");
        } else {
            response = (ResponsePojo) getIntent().getSerializableExtra("response");
            // mClinic=savedInstanceState.getSerializable("response");
        }

        buildPages();
    }

    private void setField() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("response", response);
        outState.putSerializable("clinic", mClinic);
        super.onSaveInstanceState(outState);
    }

    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        bookingFragment = new BookingFragment();
        searchClinicFragment = new SearchClinicFragment();

        Bundle data = new Bundle();
        data.putSerializable("response", response);
        data.putSerializable("bookings", (java.io.Serializable) response.getBookingList());
        bookingFragment.setArguments(data);
        searchClinicFragment.setArguments(data);

        pageFragmentList.add(bookingFragment);
        // pageFragmentList.add(searchClinicFragment);

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
                    if (pf instanceof BookingFragment) {
                        isBack = false;

                        pageFragmentList.remove(searchClinicFragment);
                        adapter.notifyDataSetChanged();
                    } else if (pf instanceof SearchClinicFragment) {
                        isBack = false;

                    }


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
            Log.e(LOG, "-- Some shit happened, probably IllegalState of some kind ...", e);
        }
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

    ClinicDTO mClinic;
    private int currentViewPager;
    boolean isBack;

    @Override
    public void onClinicSelected(ClinicDTO clinic) {
        mClinic = clinic;
        bookingFragment.updateTown(mClinic);
        pageFragmentList.remove(searchClinicFragment);
        adapter.notifyDataSetChanged();
        currentViewPager = 0;
        isBack = true;
        mPager.setCurrentItem(currentViewPager, true);
    }

    @Override
    public void onClinicRequest() {
        currentViewPager = 1;
        isBack = false;

        pageFragmentList.add(searchClinicFragment);
        adapter.notifyDataSetChanged();
        mPager.setCurrentItem(currentViewPager, true);
    }

    @Override
    public void onBooked() {

    }

    @Override
    public void onBackPressed() {
        currentViewPager = 0;
        mPager.setCurrentItem(currentViewPager, true);
        if (isBack) {
            super.onBackPressed();
        }
        isBack = true;
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
                    title = "Make booking";
                    break;
                case 1:
                    title = "Search clinic";
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


    static final String LOG = "BookingActivity";


}
