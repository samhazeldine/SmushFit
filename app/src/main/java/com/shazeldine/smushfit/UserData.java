package com.shazeldine.smushfit;

// Created by Samuel Hazeldine on 27/02/2018.
// Stores all the user data as List of EntriesOfAttributes

import android.app.Application;

import java.util.ArrayList;

public class UserData {

    private static ArrayList<EntriesOfAttribute> data = new ArrayList<>();

    private static String[] attributes =
            {"sleep", "steps", "distracting_min", "events", "mood", "productive_min", "sleep_awakenings", "tracks"};
    private static double[] goals =
            {480.0, 10000.0, 30.0, -1.0, 5.0, 120.0, 2.0, 30.0};

    public static ArrayList<EntriesOfAttribute> getData() {
        return data;
    }

    public static String[] getAttributes() {
        return attributes;
    }

    //Goals for a specific attribute.
    //A goal of -1 means no goal is set.
    public static double getGoalForAttribute(String attr) {
        for (int i=0; i < attributes.length; i ++) {
            if(attr.equals(attributes[i])) {
                return goals[i];
            }
        }
        return -1;
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
