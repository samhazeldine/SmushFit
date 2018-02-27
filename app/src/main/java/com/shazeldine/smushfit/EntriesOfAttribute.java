package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 27/02/2018.
// Used to store all entries of a specific attribute


import java.util.ArrayList;
import java.util.List;

public class EntriesOfAttribute {
    private String attributeType;
    private List<Entry> entries;

    public EntriesOfAttribute (String attributeType) {
        this.attributeType = attributeType;
        entries = new ArrayList();
    }

    public void addEntry (String attributeType, Entry entry) {
        if(this.attributeType.equals(attributeType)) {
            entries.add(entry);
        }
    }

    public String getAttributeType () {
        return attributeType;
    }

    // Returns the entry that matches a specific date
    // TODO Need to add exception case instead of null.
    public Entry getEntryForDate (String date) {
        for(Entry entry : entries) {
            if (entry.getDate().equals(date)) {
                return entry;
            }
        }
        return null;
    }


}

