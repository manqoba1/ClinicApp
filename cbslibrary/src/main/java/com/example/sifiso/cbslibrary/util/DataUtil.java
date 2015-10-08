package com.example.sifiso.cbslibrary.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.sifiso.cbslibrary.models.BookingDTO;
import com.example.sifiso.cbslibrary.models.ClinicDTO;
import com.example.sifiso.cbslibrary.models.GcmDeviceDTO;
import com.example.sifiso.cbslibrary.models.PatientDTO;
import com.example.sifiso.cbslibrary.models.RequestPojo;
import com.example.sifiso.cbslibrary.models.ResponsePojo;
import com.example.sifiso.cbslibrary.models.TownDTO;
import com.example.sifiso.cbslibrary.toolbox.BaseVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sifiso on 2015-05-02.
 */
public class DataUtil {
    static DataUtilInterface dataUtilInterface;

    public interface DataUtilInterface {
        public void onResponse(JSONObject r);

        public void onError(String error);

    }

    public static void login(final Context ctx, String email, String pin, GcmDeviceDTO dto, final DataUtilInterface listener) {

        dataUtilInterface = listener;
        RequestPojo req = new RequestPojo();
        req.setEmail(email);
        req.setPassword(pin);
        req.setGcmDevice(dto);

        try {
            BaseVolley.getRemoteData(Constant.LOGIN, req, ctx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject response) {
                    try {
                        if (response.getInt("success") < 0) {
                            Toast.makeText(ctx, response.getString("message"), Toast.LENGTH_LONG).show();
                            listener.onError(response.getString("message"));
                            return;
                        }
                        listener.onResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    listener.onError(error.toString());
                }
            });
        } catch (Exception e) {

        }
    }

    public static void searchClinic(final Context ctx, int townID, final DataUtilInterface listener) {

        dataUtilInterface = listener;
        RequestPojo req = new RequestPojo();
        req.setTownID(townID);

        try {
            BaseVolley.getRemoteData(Constant.SEARCH_CLINIC, req, ctx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject response) {
                    try {
                        if (response.getInt("success") < 0) {
                            Toast.makeText(ctx, response.getString("message"), Toast.LENGTH_LONG).show();
                            listener.onError(response.getString("message"));
                            return;
                        }
                        listener.onResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    listener.onError(error.toString());
                }
            });
        } catch (Exception e) {

        }
    }

    public static void dashData(final Context ctx, int patientId, final DataUtilInterface listener) {

        dataUtilInterface = listener;
        RequestPojo req = new RequestPojo();
        req.setPatientID(patientId);

        try {
            BaseVolley.getRemoteData(Constant.DASH_DATA, req, ctx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject response) {
                    try {
                        if (response.getInt("success") < 0) {
                            Toast.makeText(ctx, response.getString("message"), Toast.LENGTH_LONG).show();
                            listener.onError(response.getString("message"));
                            return;
                        }
                        listener.onResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    listener.onError(error.toString());
                }
            });
        } catch (Exception e) {

        }
    }

    public static void registerPatient(final Context ctx, PatientDTO patient, final DataUtilInterface listener) {

        dataUtilInterface = listener;
        RequestPojo req = new RequestPojo();
        req.setPatient(patient);

        try {
            BaseVolley.getRemoteData(Constant.REGISTER_PATIENT, req, ctx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject response) {
                    try {
                        if (response.getInt("success") < 0) {
                            Toast.makeText(ctx, response.getString("message"), Toast.LENGTH_LONG).show();
                            listener.onError(response.getString("message"));
                            return;
                        }
                        listener.onResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    listener.onError(error.toString());
                }
            });
        } catch (Exception e) {

        }
    }

    public static void makeBooking(final Context ctx, BookingDTO booking, final DataUtilInterface listener) {

        dataUtilInterface = listener;
        RequestPojo req = new RequestPojo();
        req.setBooking(booking);

        try {
            BaseVolley.getRemoteData(Constant.MAKE_BOOKING, req, ctx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject response) {
                    try {
                        if (response.getInt("success") == 0) {
                            Toast.makeText(ctx, response.getString("message"), Toast.LENGTH_LONG).show();
                            listener.onError(response.getString("message"));
                            return;
                        }
                        listener.onResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    listener.onError(error.toString());
                }
            });
        } catch (Exception e) {

        }
    }

    public static List<ClinicDTO> clinicList(JSONArray ar) throws JSONException {
        List<ClinicDTO> list = new ArrayList<>();
        for (int i = 0; i < ar.length(); i++) {
            JSONObject o = ar.getJSONObject(i);
            ClinicDTO dto = new ClinicDTO();
            dto.setClinicID(o.getInt("clinicID"));
            dto.setPhoneNumber(o.getString("phoneNumber"));
            dto.setEmail(o.getString("email"));
            dto.setLatitude(o.getDouble("latitude"));
            dto.setLongitude(o.getDouble("longitude"));
            dto.setName(o.getString("name"));
            dto.setTownName(o.getString("townName"));
            list.add(dto);
        }
        return list;
    }

    public interface JsonifyListener {
        public void onResponseJSon(ResponsePojo resp);

        public void onError(String error);
    }

    static JsonifyListener mListener;
    static JSONObject mObject;
    static Context mCtx;

    public static void getData(Context ctx, JSONObject object, JsonifyListener listener) {
        mListener = listener;
        mObject = object;
        mCtx = ctx;
        new ObjectifyPatient().execute(mObject);

    }

    /*AsyncTask method to process the JSon response from server to response pojo*/
    static class ObjectifyPatient extends AsyncTask<JSONObject, Void, ResponsePojo> {

        private PatientDTO patient(JSONArray js) throws JSONException {
            PatientDTO dto = new PatientDTO();
            JSONObject ob = js.getJSONObject(0);
            dto.setEmail(ob.getString("email"));
            dto.setPhoneNumber(ob.getString("phoneNumber"));
            dto.setFirstName(ob.getString("firstName"));
            dto.setMiddleName(ob.getString("middleName"));
            dto.setLastName(ob.getString("lastName"));
            dto.setPassword(ob.getString("password"));
            dto.setPatientID(ob.getInt("patientID"));
            dto.setBookingList(bookingDTOList(ob.getJSONArray("booking")));

            return dto;
        }

        private List<TownDTO> townDTOs(JSONArray ar) throws JSONException {
            List<TownDTO> list = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject o = ar.getJSONObject(i);
                TownDTO d = new TownDTO();
                d.setTownName(o.getString("name"));
                d.setTownID(o.getInt("townID"));
                d.setProvinceID(o.getInt("provinceID"));
                d.setLatitude(o.getDouble("latitude"));
                d.setLongitude(o.getDouble("longitude"));
                list.add(d);
            }
            return list;
        }

        private List<BookingDTO> bookingDTOList(JSONArray ar) throws JSONException {
            List<BookingDTO> list = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject ob = ar.getJSONObject(i);
                BookingDTO dto = new BookingDTO();
                dto.setBookingID(ob.getInt("bookingID"));
                dto.setBookingDate(ob.getString("bookingDate"));
                dto.setDateAttended(ob.getString("dateAttended"));
                dto.setRefNumber(ob.getString("refNumber"));
                dto.setPatientID(ob.getInt("patientID"));
//                dto.setClinicID(ob.getInt("clinicID"));
                dto.setFlag(ob.getInt("flag"));
                dto.setSlots(ob.getString("slots"));
                dto.setTimeSlotID(ob.getInt("timeSlotID"));
                dto.setClinic(clinicDTO(ob.getJSONArray("clinic")));
                list.add(dto);
            }
            return list;
        }


        private ClinicDTO clinicDTO(JSONArray ar) throws JSONException {
            ClinicDTO dto = new ClinicDTO();
            JSONObject o = ar.getJSONObject(0);
            dto.setClinicID(o.getInt("clinicID"));
            dto.setTownID(o.getInt("townID"));
            dto.setEmail(o.getString("email"));
            dto.setName(o.getString("name"));
            dto.setLatitude(o.getDouble("latitude"));
            dto.setLongitude(o.getDouble("longitude"));
            dto.setPhoneNumber(o.getString("phoneNumber"));
            dto.setProvinceID(o.getInt("provinceID"));
            dto.setTownName(o.getString("townName"));
            return dto;
        }

        @Override
        protected ResponsePojo doInBackground(JSONObject... params) {
            JSONObject dto = params[0];
            ResponsePojo response = new ResponsePojo();
            try {

                response.setBookingList(bookingDTOList(dto.getJSONArray("booking")));
                response.setTownList(townDTOs(dto.getJSONArray("town")));
                response.setSuccess(dto.getInt("success"));
                response.setMessage(dto.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(ResponsePojo response) {
            if (response.getSuccess() == 0) {
                mListener.onError(response.getMessage());
                return;
            }
            mListener.onResponseJSon(response);
            Log.d(LOG, response.toString());
        }
    }


    /*private void checkVirgin() {
        //SharedUtil.clearTeam(ctx);
        TeamMemberDTO dto = SharedUtil.getTeamMember(ctx);
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
    }*/
    private static String LOG = DataUtil.class.getSimpleName();
}
