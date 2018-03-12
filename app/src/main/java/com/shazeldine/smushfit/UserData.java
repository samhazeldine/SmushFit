package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 27/02/2018.
// Stores all the user data as List of EntriesOfAttributes

import android.app.Application;

import java.util.ArrayList;

public class UserData {

    private static ArrayList<EntriesOfAttribute> data = new ArrayList<>();


    public static ArrayList<EntriesOfAttribute> getData() {
        return data;
    }

    //Add data to the correct EntriesOfAttribute
    public static void addDataForAttribute(String attribute, Entry entry) {
        Boolean added = false;
        for (EntriesOfAttribute entries: data) {
            if(entries.getAttributeType().equals(attribute)) {
                entries.addEntry(attribute, entry);
                added = true;
            }
        }
        if (added == false) {
            EntriesOfAttribute entriesOfAttribute = new EntriesOfAttribute(attribute);
            entriesOfAttribute.addEntry(attribute, entry);
            data.add(entriesOfAttribute);
        }
    }

    //Gets the EntriesOfAttribute for a specific attribute
    //TODO Need to add exception case instead of null
    public static EntriesOfAttribute getEntriesOfAttribute(String attribute) {
        for(EntriesOfAttribute entries : data) {
            if(entries.getAttributeType().equals(attribute)) {
                return entries;
            }
        }
        return null;
    }


}
