package com.example.sifiso.cbslibrary.models;

import java.io.Serializable;

/**
 * Created by sifiso on 2015-05-04.
 */
public class CommentDTO implements Serializable {
    private Integer commentsID, clinicID, patientID;
    private String comment, commentsDate;

    public Integer getCommentsID() {
        return commentsID;
    }

    public void setCommentsID(Integer commentsID) {
        this.commentsID = commentsID;
    }

    public Integer getClinicID() {
        return clinicID;
    }

    public void setClinicID(Integer clinicID) {
        this.clinicID = clinicID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentsDate() {
        return commentsDate;
    }

    public void setCommentsDate(String commentsDate) {
        this.commentsDate = commentsDate;
    }
}
