package com.shazeldine.smushfit;

/**
 * Created by Samuel Hazeldine on 02/03/2018.
 */

public class Insight {
    private int id;
    private String content;

    public Insight (int id, String content) {
        this.id = id;
        this. content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
