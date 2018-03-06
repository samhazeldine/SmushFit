package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 21/02/2018.
// Defies the Bean for Android


import java.text.DateFormat;

public class Entry {

    private String date;

    private String value;

    public Entry(String date, String value) {
        this.value = value;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

}
