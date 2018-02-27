package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 27/02/2018.
// Used to store all entries of a specific attribute


import java.util.List;

public class EntriesOfAttribute {
    private String attributeType;
    private List<Entry> entries;

    public EntriesOfAttribute (String attributeType) {
        this.attributeType = attributeType;
    }

    public void addEntry (String attributeType, Entry entry) {
        if(this.attributeType == attributeType) {
            entries.add(entry);
        }
    }

    public String getAttributeType () {
        return this.getAttributeType();
    }


}

