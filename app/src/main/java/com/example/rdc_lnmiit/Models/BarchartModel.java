package com.example.rdc_lnmiit.Models;

public class BarchartModel {

    String schemename;
    int noOfPeopleEnrolled;

    public BarchartModel() {

    }

    public BarchartModel(String schemename, int noOfPeopleEnrolled) {
        this.schemename = schemename;
        this.noOfPeopleEnrolled = noOfPeopleEnrolled;
    }

    public String getSchemename() {
        return schemename;
    }

    public long getNoOfPeopleEnrolled() {
        return noOfPeopleEnrolled;
    }
}
