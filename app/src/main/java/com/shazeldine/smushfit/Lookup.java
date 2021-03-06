package com.shazeldine.smushfit;

import android.util.Log;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by Samuel Hazeldine on 22/02/2018.
 */

public class Lookup {


    //Gets the non empty values for two attributes and combines into a list of String doubles
    //Naively assumes that dates are the in the same order for each.
    public List<String[]> getNonEmptyValueOfTwoAttributes(String attr1, String attr2) {
        EntriesOfAttribute entriesAttr1 = UserData.getEntriesOfAttribute(attr1);
        EntriesOfAttribute entriesAttr2 = UserData.getEntriesOfAttribute(attr2);
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
    public List<String> getNonEmptyValuesOfAttribute(String attr){
        EntriesOfAttribute entriesAttr1 = UserData.getEntriesOfAttribute(attr);
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

    // Uses the Apache commons correlation function.
    // Only works when the Attributes are doubles
    public double findCorrelation (String attr1, String attr2) {
        List<String[]> nonEmptyValues = getNonEmptyValueOfTwoAttributes(attr1, attr2);
        double[][] correlationValues = listOfStringArrayToDoubleArray(nonEmptyValues);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlationPValue = pearsonsCorrelation.correlation(correlationValues[0], correlationValues[1]);
        return correlationPValue;
    }

    // Finds current value for insight
    public double findCurrent (String attr) {
        List<String> nonEmptyValues = getNonEmptyValuesOfAttribute(attr);
        double[] doubleValues = listOfStringToDoubleArray(nonEmptyValues);
        double current = doubleValues[doubleValues.length - 1];
        return current;
    }

    // Uses Apache Commons library to find mean values for an attribute
    public double findMean (String attr) {
        List<String> nonEmptyValues = getNonEmptyValuesOfAttribute(attr);
        double[] doubleValues = listOfStringToDoubleArray(nonEmptyValues);
        Mean meanCalc = new Mean();
        double mean = meanCalc.evaluate(doubleValues);
        return mean;
    }

    // Uses Apache Commons library to find max value of an attribute
    public double findMax(String attr) {
        List<String> nonEmptyValues = getNonEmptyValuesOfAttribute(attr);
        double[] doubleValues = listOfStringToDoubleArray(nonEmptyValues);
        Max maxCalc = new Max();
        double max = maxCalc.evaluate(doubleValues);
        return max;
    }

    // Uses Apache Commons library to find min value of an attribute
    public double findMin(String attr) {
        List<String> nonEmptyValues = getNonEmptyValuesOfAttribute(attr);
        double[] doubleValues = listOfStringToDoubleArray(nonEmptyValues);
        Min minCalc = new Min();
        double max = minCalc.evaluate(doubleValues);
        return max;
    }

    // Uses Apache Commons library to find trend slope
    public double findTrend(String attr) {
        List<String> nonEmptyValues = getNonEmptyValuesOfAttribute(attr);
        double[] doubleValues = listOfStringToDoubleArray(nonEmptyValues);
        SimpleRegression regression = new SimpleRegression();
        for (int i=0; i<nonEmptyValues.size(); i++) {
            regression.addData(i, doubleValues[i]);
        }
        double result = regression.getSlope();
        String s = "The slope is: " + result;
        Log.i("TEST", s);
        return result;
    }


    // Converts a list of String arrays into a 2D array of doubles
    // TODO - add exceptions
    private double[][] listOfStringArrayToDoubleArray(List<String[]> nonEmptyValues) {
        double[][] doubleValues = new double[2][nonEmptyValues.size()];
        for(int i = 0; i < nonEmptyValues.size(); i++) {
            String[] values = nonEmptyValues.get(i);
            doubleValues[0][i] = Double.parseDouble(values[0]);
            doubleValues[1][i] = Double.parseDouble(values[1]);
        }
        return doubleValues;
    }

    // Converts a list of Strings into an array of doubles
    // TODO - add exceptions
    private double[] listOfStringToDoubleArray(List<String> nonEmptyValues) {
        double[] doubleValues = new double[nonEmptyValues.size()];
        for(int i = 0; i < nonEmptyValues.size(); i++) {
            String value = nonEmptyValues.get(i);
            doubleValues[i] = Double.parseDouble(value);
        }
        return doubleValues;
    }

    //Finds the correlations between every other attribute and "attr"
    //What is affected by x?
    public List<CorrelationIdentifier> findGeneralCorrelation(String attr, double pearsonParameter) {
        String[] attributes = UserData.getAttributes();
        List<CorrelationIdentifier> correlationList = new ArrayList<>();
        for(int i = 0; i < attributes.length; i++) {
            if(!attr.equals(attributes[i])) {
                double pearsonValue = findCorrelation(attr, attributes[i]);
                if(abs(pearsonValue)>pearsonParameter) {
                    correlationList.add(new CorrelationIdentifier(attr, attributes[i], pearsonValue));
                }
            }
        }
        return correlationList;
    }

    //Finds the "likely" correlations between every other attribute and "attr"
    public List<CorrelationIdentifier> findLikelyCorrelation(String attr) {
        List<CorrelationIdentifier> correlationList = findGeneralCorrelation(attr, 0.55);
        return correlationList;
    }

    // 0 - attribute, 1 - met, onTrack, littleMore, failed
    public List<String[]> findAllGoals() {
        String[] attributes = UserData.getAttributes();
        List<String[]> goalProgresses = new ArrayList<>();
        for(int i = 0; i < attributes.length; i++) {
            EntriesOfAttribute entry = UserData.getEntriesOfAttribute(attributes[i]);
            String attribute = attributes[i];
            double current = findCurrent(attribute);
            double goal = UserData.getGoalForAttribute(attribute);
            String goalAim = entry.getGoalAim();
            if(!goalAim.equals("None")) {
                if(goalAim.equals("High") && (current >= goal)) {
                    String[] temp = {attribute, "met"};
                    goalProgresses.add(temp);
                }
                else if(goalAim.equals("High") && (current < goal)) {
                    String[] temp = {attribute, "littleMore"};
                    goalProgresses.add(temp);
                }
                else if(goalAim.equals("Low") && (current > goal)) {
                    String[] temp = {attribute, "failed"};
                    goalProgresses.add(temp);
                }
                else {
                    String[] temp = {attribute, "onTrack"};
                    goalProgresses.add(temp);
                }
            }
        }
        return goalProgresses;
    }
}
