package com.shazeldine.smushfit;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.io.Serializable;
import java.util.Date;

// Created by Samuel Hazeldine on 21/02/2018.
// Defies the Bean for Android

public class Entry implements Serializable {

    @CsvBindByName
    private String attribute;

    @CsvBindByName
    @CsvDate("yyyy.MM.dd")
    private Date date;

    @CsvBindByName
    private String value;

    public void Entry() {

    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
