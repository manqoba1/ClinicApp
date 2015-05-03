package com.example.sifiso.cbslibrary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sifiso.cbslibrary.R;
import com.example.sifiso.cbslibrary.adapter.AppointmentAdapter;
import com.example.sifiso.cbslibrary.models.BookingDTO;
import com.example.sifiso.cbslibrary.models.ResponsePojo;

import java.util.ArrayList;
import java.util.List;


public class AppointmentListFragment extends Fragment implements PageFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private AppointmentListFragmentListener mListener;


    private LinearLayout FEL_search;
    private ListView FEL_list;
    private ImageView SLT_imgSearch2, SLT_hero;
    private EditText SLT_editSearch;

    public AppointmentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    View v;
    AppointmentAdapter adapter;

    private Context ctx;
    ResponsePojo response;
    private List<BookingDTO> bookingList;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        Bundle b = getArguments();
        //setHasOptionsMenu(true);
        //  getActivity().setTheme(R.style.EvalListTheme);
        ctx = getActivity().getApplicationContext();
        activity = getActivity();
        if (b != null) {
            bookingList = (List<BookingDTO>) b.getSerializable("bookings");
            response = (ResponsePojo) b.getSerializable("response");


        }
        build();
        return v;
    }

    private void build() {

        FEL_list = (ListView) v.findViewById(R.id.FEL_list);
        FEL_search = (LinearLayout) v.findViewById(R.id.FEL_search);
        SLT_editSearch = (EditText) v.findViewById(R.id.SLT_editSearch);
        SLT_editSearch.setVisibility(View.GONE);
        SLT_hero = (ImageView) v.findViewById(R.id.SLT_hero);
        SLT_imgSearch2 = (ImageView) v.findViewById(R.id.SLT_imgSearch2);
        SLT_imgSearch2.setVisibility(View.GONE);
        //SLT_hero.setImageDrawable(Util.getRandomHeroImage(ctx));


        setList();
    }
    private void setList() {
        if (bookingList == null) bookingList = new ArrayList<>();

        adapter = new AppointmentAdapter(ctx, R.layout.visit_view_custom, bookingList, new AppointmentAdapter.AppointmentAdapterListener() {
            @Override
            public void onVisitData(BookingDTO visit) {

            }
        });
        FEL_list.setAdapter(adapter);
    }


    @Override
    public void animateCounts() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AppointmentListFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
