package com.example.sifiso.cbslibrary.models;

import java.io.Serializable;

/**
 * Created by sifiso on 2015-05-04.
 */
public class TimeSlotDTO implements Serializable {
    private Integer timeSlotID;
    private String slots;

    public Integer getTimeSlotID() {
        return timeSlotID;
    }

    public void setTimeSlotID(Integer timeSlotID) {
        this.timeSlotID = timeSlotID;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }
}
