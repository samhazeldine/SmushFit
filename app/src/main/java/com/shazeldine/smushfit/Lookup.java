package com.shazeldine.smushfit;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel Hazeldine on 22/02/2018.
 */

public class Lookup {

    public String getValueForAttributeAtDate(UserData userData, String attribute, String date) {
        Entry entry = userData.getEntriesOfAttribute(attribute).getEntryForDate(date);
        String value = entry.getValue();
        return value;
    }
}
