package com.shazeldine.smushfit;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Samuel Hazeldine on 05/03/2018.
 */

public class Content {
    public static final List<Insight> items = new ArrayList();
    private static final Map<String, Insight> itemMap = new HashMap();

    public static void addItem(Insight insight) {
        items.add(insight);
        itemMap.put(insight.getId(), insight);
    }

    public static Insight createInsight() {
        return new Insight("1", "WOOOOOOO");
    }

    static {
        addItem(createInsight());
    }

    public static class Insight {
        private String id;
        private String content;

        public Insight(String id, String content) {
            this.id = id;
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}
