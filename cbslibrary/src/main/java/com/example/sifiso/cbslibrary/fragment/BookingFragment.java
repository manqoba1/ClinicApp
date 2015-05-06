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
import android.widget.Button;
import android.widget.TextView;

import com.example.sifiso.cbslibrary.R;
import com.example.sifiso.cbslibrary.models.BookingDTO;
import com.example.sifiso.cbslibrary.models.ClinicDTO;
import com.example.sifiso.cbslibrary.models.PatientDTO;
import com.example.sifiso.cbslibrary.models.RequestPojo;
import com.example.sifiso.cbslibrary.util.DataUtil;
import com.example.sifiso.cbslibrary.util.SharedUtil;
import com.example.sifiso.cbslibrary.util.Util;

import org.json.JSONException;
import org.json.JSONObject;


public class BookingFragment extends Fragment implements PageFragment {
    Context ctx;
    Activity activity;
    Button btn_someone, btn_me_book;
    TextView Sp_clinic;
    private Integer clinicID, patientID;
    PatientDTO patient;
    private BookingFragmentListener mListener;

    static String LOG = BookingFragment.class.getSimpleName();

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void updateTown(ClinicDTO t) {
        Log.d(LOG, t.getClinicID() + " : " + t.getName());
        if (Sp_clinic == null) {
            Sp_clinic = (TextView) v.findViewById(R.id.Sp_clinic);
        }
        Sp_clinic.setText(t.getName());
        clinicID = t.getClinicID();
    }

    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        ctx = getActivity().getApplicationContext();
        v = inflater.inflate(R.layout.fragment_booking, container, false);
        patient = SharedUtil.getPatient(ctx);
        patientID = patient.getPatientID();
        setFields();
        return v;
    }

    public void setFields() {
        btn_someone = (Button) v.findViewById(R.id.btn_someone);
        btn_me_book = (Button) v.findViewById(R.id.btn_me_book);
        Sp_clinic = (TextView) v.findViewById(R.id.Sp_clinic);
        Sp_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClinicRequest();
            }
        });
        btn_me_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeBooking();
            }
        });
    }

    private void makeBooking() {
        if (patientID == null) {
            Util.showErrorToast(ctx, "Patient reequired");
            return;
        }
        if (clinicID == null) {
            Util.showErrorToast(ctx, "Clinic reequired");
            return;
        }
        BookingDTO dto = new BookingDTO();
        dto.setClinicID(clinicID);
        dto.setPatientID(patientID);

        DataUtil.makeBooking(ctx, dto, new DataUtil.DataUtilInterface() {
            @Override
            public void onResponse(JSONObject r) {
                try {
                    if (r.getInt("success") == 0) {
                        Util.showErrorToast(ctx, r.getString("message"));
                        return;
                    }
                    Util.showErrorToast(ctx, r.getString("message"));

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
            mListener = (BookingFragmentListener) activity;
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

    @Override
    public void animateCounts() {

    }


    public interface BookingFragmentListener {
        // TODO: Update argument type and name
        public void onClinicRequest();

        public void onBooked();
    }

}
