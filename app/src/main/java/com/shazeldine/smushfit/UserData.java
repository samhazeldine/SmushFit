package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 27/02/2018.
// Stores all the user data as List of EntriesOfAttributes

import java.util.ArrayList;

public class UserData {

    private ArrayList<EntriesOfAttribute> userData;

    public UserData() {
        userData = new ArrayList<EntriesOfAttribute>();
    }

    public void addDataForAttribute(String attribute, Entry entry) {
        if (userData.size() == 0) {
            userData.add(new EntriesOfAttribute(attribute));
        }
        else if (addToCorrectAttribute(attribute, entry)) {
        }
        else {
            userData.add(new EntriesOfAttribute(attribute));
        }
    }

    public Boolean addToCorrectAttribute(String attribute, Entry entry) {
        for(EntriesOfAttribute entries : userData) {
            if (entries.getAttributeType().equals(attribute)) {
                entries.addEntry(attribute, entry);
                return true;
            }
        }
        return false;
    }

    //Gets the EntriesOfAttribute for a specific attribute
    public EntriesOfAttribute getEntriesOfAttribute(String attribute) {
        for(EntriesOfAttribute entries : userData) {
            if(entries.getAttributeType().equals(attribute)) {
                return entries;
            }
        }
        return null;
    }



}
