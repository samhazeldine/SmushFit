package com.shazeldine.smushfit;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
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

    //Gets the non empty values for two attributes and combines into a list of String doubles
    //Naively assumes that dates are the in the same order for each.
    public List<String[]> getNonEmptyValueOfTwoAttributes(UserData userData, String attr1, String attr2) {
        EntriesOfAttribute entriesAttr1 = userData.getEntriesOfAttribute(attr1);
        EntriesOfAttribute entriesAttr2 = userData.getEntriesOfAttribute(attr2);
        Iterator<Entry> entryIterator1 = entriesAttr1.getEntries().iterator();
        Iterator<Entry> entryIterator2 = entriesAttr2.getEntries().iterator();
        List<String[]> nonEmptyValues = new ArrayList();
        while(entryIterator1.hasNext() && entryIterator2.hasNext()) {
            Entry entry1 = entryIterator1.next();
            Entry entry2 = entryIterator2.next();
            String value1 = entry1.getValue();
            String value2 = entry2.getValue();
            if (!value1.equals("None") && !value2.equals("None")) {
             String[] tempStrings = {value1, value2};
             nonEmptyValues.add(tempStrings);
            }
        }
        return nonEmptyValues;
    }

    //Gets the non empty values for an attribute and combines into a list of Strings
    public List<String> getNonEmptyValuesOfAttribute(UserData userData, String attr){
        EntriesOfAttribute entriesAttr1 = userData.getEntriesOfAttribute(attr);
        Iterator<Entry> entryIterator1 = entriesAttr1.getEntries().iterator();
        List<String> nonEmptyValues = new ArrayList();
        while(entryIterator1.hasNext()) {
            Entry entry1 = entryIterator1.next();
            String value1 = entry1.getValue();
            if (!value1.equals("None")) {
                nonEmptyValues.add(value1);
            }
        }
        return nonEmptyValues;
    }

    public double findCorrelation (UserData userData, String attr1, String attr2) {
        List<String[]> nonEmptyValues = getNonEmptyValueOfTwoAttributes(userData, attr1, attr2);
        double[][] correlationValues = listOfStringToDoubleArray(nonEmptyValues);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlationPValue = pearsonsCorrelation.correlation(correlationValues[0], correlationValues[1]);
        return correlationPValue;
    }

    private double[][] listOfStringToDoubleArray(List<String[]> nonEmptyValues) {
        double[][] doubleValues = new double[2][nonEmptyValues.size()];
        for(int i = 0; i < nonEmptyValues.size(); i++) {
            String[] values = nonEmptyValues.get(i);
            doubleValues[0][i] = Double.parseDouble(values[0]);
            doubleValues[1][i] = Double.parseDouble(values[1]);
        }
        return doubleValues;
    }
}
