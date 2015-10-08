package com.example.sifiso.cbslibrary.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sifiso.cbslibrary.R;
import com.example.sifiso.cbslibrary.adapter.SearchClinicAdapter;
import com.example.sifiso.cbslibrary.models.BookingDTO;
import com.example.sifiso.cbslibrary.models.ClinicDTO;
import com.example.sifiso.cbslibrary.models.ResponsePojo;
import com.example.sifiso.cbslibrary.models.TownDTO;
import com.example.sifiso.cbslibrary.util.DataUtil;
import com.example.sifiso.cbslibrary.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchClinicFragment extends Fragment implements PageFragment {
    private ImageView SLT_imgSearch2, SLT_hero;
    AutoCompleteTextView SLT_editSearch;
    ListView STF_list;
    SearchClinicAdapter adapter;
    SearchClinicFragmentListener mListener;
    Context ctx;
    Activity activity;
    View v;
    List<ClinicDTO> clinicList;
    ResponsePojo response;
    boolean isFound;
    String searchText;
    List<BookingDTO> bookingList;


    public SearchClinicFragment() {
        // Required empty public constructor
    }

    private void setFields() {
        SLT_editSearch = (AutoCompleteTextView) v.findViewById(R.id.SLT_editSearch);
        SLT_imgSearch2 = (ImageView) v.findViewById(R.id.SLT_imgSearch2);
        SLT_hero = (ImageView) v.findViewById(R.id.SLT_hero);
        STF_list = (ListView) v.findViewById(R.id.STF_list);
        SLT_imgSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchRiver();
            }
        });

        //SLT_hero.setImageDrawable(Util.getRandomHeroImage(ctx));
        setClinicListFromBooking();
        riverToSearch();
    }

    private void setList() {

        adapter = new SearchClinicAdapter(ctx, clinicList, new SearchClinicAdapter.SearchTownAdapterListener() {
            @Override
            public void onTownClicked(ClinicDTO town) {

            }
        });
        STF_list.setAdapter(adapter);
        STF_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClinicDTO clinic = (ClinicDTO) parent.getItemAtPosition(position);
                mListener.onClinicSelected(clinic);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    List<String> stringRiver;

    private void riverToSearch() {
        stringRiver = new ArrayList<>();
        for (int i = 0; i < response.getTownList().size(); i++) {

            TownDTO town = response.getTownList().get(i);
            stringRiver.add(town.getTownName());
        }
        ArrayAdapter<String> riverSearchAdapter = new ArrayAdapter<String>(ctx, R.layout.xsimple_spinner_dropdown_item, stringRiver);
        SLT_editSearch.setAdapter(riverSearchAdapter);
    }

    private void searchRiver() {
        if (SLT_editSearch.getText().toString().isEmpty()) {
            return;
        }
        int index = 0;
        searchText = SLT_editSearch.getText().toString();
        TownDTO searchClinic = null;
        for (int i = 0; i < response.getTownList().size(); i++) {
            searchClinic = response.getTownList().get(i);
            if (searchClinic.getTownName().contains(searchText)) {
                isFound = true;
                break;
            }
            index++;
        }
        if (isFound) {
            // STF_list.setSelection(index);
            int townID = searchClinic.getTownID();
            setSearchedClinicList(townID);
        } else {
            Util.showToast(ctx, ctx.getString(R.string.river_not_found) + " " + SLT_editSearch.getText().toString());
        }
        hideKeyboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_clinic, container, false);
        ctx = getActivity().getApplicationContext();
        response = (ResponsePojo) getArguments().getSerializable("response");
        bookingList = (List<BookingDTO>) getArguments().getSerializable("bookings");
        activity = getActivity();
        setFields();
        return v;
    }

    private void setClinicListFromBooking() {
        if (clinicList == null) {
            clinicList = new ArrayList<>();
        }
        if (bookingList != null) {
            Log.d(LOG, bookingList.toString());
            for (int i = 0; i < bookingList.size(); i++) {
                BookingDTO dto = bookingList.get(i);

                if (clinicList.contains(dto.getClinic())) {
                    //   clinicList.remove(i);
                }
                clinicList.add(dto.getClinic());
            }
            setList();
        }
    }

    static String LOG = SearchClinicFragment.class.getSimpleName();

    private void setSearchedClinicList(int townID) {
        DataUtil.searchClinic(ctx, townID, new DataUtil.DataUtilInterface() {
            @Override
            public void onResponse(JSONObject r) {
                try {
                    if (r.getInt("success") == 0) {
                        Util.showErrorToast(ctx, r.getString("message"));
                        return;
                    }
                    if (clinicList == null) {
                        clinicList = new ArrayList<ClinicDTO>();
                    }
                    clinicList = DataUtil.clinicList(r.getJSONArray("clinic"));
                    Log.d(LOG, clinicList.toString());
                    setList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SearchClinicFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(SLT_editSearch.getWindowToken(), 0);
    }

    @Override
    public void animateCounts() {

    }

    public interface SearchClinicFragmentListener {

        public void onClinicSelected(ClinicDTO clinic);
    }


}
