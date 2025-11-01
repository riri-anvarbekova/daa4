package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testBuildGraphFromJson() {
        JSONObject obj = new JSONObject();
        obj.put("n", 3);
        JSONArray edges = new JSONArray();
        edges.put(new JSONObject().put("u", 0).put("v", 1).put("w", 2));
        edges.put(new JSONObject().put("u", 1).put("v", 2).put("w", 3));
        obj.put("edges", edges);

        var g = App.buildGraphFromJson(obj);
        assertEquals(3, g.n());
        assertEquals(2, g.m());
    }

    @Test
    void testProcessGraphRuns() {
        JSONObject obj = new JSONObject();
        obj.put("n", 3);
        JSONArray edges = new JSONArray();
        edges.put(new JSONObject().put("u", 0).put("v", 1).put("w", 2));
        edges.put(new JSONObject().put("u", 1).put("v", 2).put("w", 3));
        obj.put("edges", edges);

        var g = App.buildGraphFromJson(obj);
        App.processGraph(g, 0, "test_graph.json");
    }
}
