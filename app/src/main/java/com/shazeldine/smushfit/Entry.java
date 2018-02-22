package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 21/02/2018.
// Defies the Bean for Android

public class Entry {

    private String attribute;

    private String date;

    private String value;

    public Entry(String attribute, String date, String value) {
        this.attribute = attribute;
        this.value = value;
        this.date = date;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

}
