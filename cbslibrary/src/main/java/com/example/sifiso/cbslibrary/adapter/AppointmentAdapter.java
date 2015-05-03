package com.example.sifiso.cbslibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sifiso.cbslibrary.R;
import com.example.sifiso.cbslibrary.models.BookingDTO;

import java.util.List;

/**
 * Created by sifiso on 2015-05-03.
 */
public class AppointmentAdapter extends ArrayAdapter<BookingDTO>{
    public interface AppointmentAdapterListener {
        public void onVisitData(BookingDTO visit);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<BookingDTO> mList;
    private Context ctx;
    private AppointmentAdapterListener listener;

    static class Holder {
        TextView status, statusReport, statusReport_date, app_date,ref_nummber;
    }

    public AppointmentAdapter(Context context, int resource, List<BookingDTO> list, AppointmentAdapterListener listener) {
        super(context, resource, list);
        this.mLayoutRes = resource;
        mList = list;
        this.listener = listener;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    Holder h;

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        h = new Holder();
        if (v == null) {
            v = mInflater.inflate(mLayoutRes, null);
            h.status = (TextView) v.findViewById(R.id.status);
            h.statusReport = (TextView) v.findViewById(R.id.statusReport);
            h.statusReport_date = (TextView) v.findViewById(R.id.statusReport_date);
            h.app_date = (TextView) v.findViewById(R.id.app_date);
            h.ref_nummber =(TextView) v.findViewById(R.id.ref_nummber);
            v.setTag(h);
        } else {
            h = (Holder) v.getTag();
        }
        final BookingDTO dto = mList.get(position);

        h.ref_nummber.setText(dto.getRefNumber());


        if (dto.getFlag() == 1) {
            h.status.setTextColor(v.getResources().getColor(R.color.absa_red));
            h.status.setText("Pending");
            h.statusReport.setVisibility(v.GONE);
            h.statusReport_date.setVisibility(v.GONE);
        } else {
            h.status.setText("Attended");
            h.status.setTextColor(v.getResources().getColor(R.color.absa_red));
            h.statusReport.setVisibility(v.VISIBLE);
            h.statusReport_date.setVisibility(v.VISIBLE);
            h.statusReport.setText("Your have to come to the clinic " + dto.getDateAttended());
        }

        h.statusReport_date.setText(dto.getBookingDate());
        h.app_date.setText(dto.getBookingDate());
        animateView(v);
        return v;
    }

    public void animateView(final View view) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.grow_fade_in_center);
        a.setDuration(2500);
        if (view == null)
            return;
        view.startAnimation(a);
    }
}
