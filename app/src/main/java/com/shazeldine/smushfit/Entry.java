package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 21/02/2018.
// Defies the Bean for Android

public class Entry {

    private String attribute;

    private String date;

    private String value;

    public Entry() {

    }

    public Entry(String attribute, String date, String value) {
        this.attribute = attribute;
        this.date = date;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
