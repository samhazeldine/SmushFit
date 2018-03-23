package com.shazeldine.smushfit;

/**
 * Created by Samuel Hazeldine on 23/03/2018.
 */

public class CorrelationIdentifier {
    private String attr1;
    private String attr2;
    private double correlationValue;

    public CorrelationIdentifier(String attr1, String attr2, double correlationValue) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.correlationValue = correlationValue;
    }

    public String getAttr1() {
        return attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public double getCorrelationValue() {
        return correlationValue;
    }
}
