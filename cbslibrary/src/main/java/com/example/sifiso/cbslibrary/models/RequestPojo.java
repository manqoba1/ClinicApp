package com.example.sifiso.cbslibrary.models;

import android.content.Intent;

/**
 * Created by sifiso on 2015-05-02.
 */
public class RequestPojo {
    private Integer requestType,townID,patientID;
    private String email, password, phoneCall;
    private GcmDeviceDTO gcmDevice;
    private PatientDTO patient;
    private BookingDTO booking;

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public void setGcmDevice(GcmDeviceDTO gcmDevice) {
        this.gcmDevice = gcmDevice;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public void setTownID(Integer townID) {
        this.townID = townID;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setPhoneCall(String phoneCall) {
        this.phoneCall = phoneCall;
    }
}
