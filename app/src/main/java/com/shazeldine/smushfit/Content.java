package com.shazeldine.smushfit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel Hazeldine on 02/03/2018.
 */

public class Content {
    private static List<Insight> items;
    int idCount;

    public Content () {
        items = new ArrayList();
        idCount = 0;
        items.add(new Insight(1, "WOOP TESTING"));
    }

    public void addItem (String insight) {
        items.add(new Insight(idCount, insight));
        idCount++;
    }

    public static List<Insight> getItems () {
        return items;
    }
}
