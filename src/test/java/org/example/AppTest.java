package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

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

        var g = Metrics_Output.buildGraphFromJson(obj);
        assertEquals(3, g.n());
        assertEquals(2, g.m());
    }

    @Test
    void testProcessGraphRuns() {
        PrintWriter pw = new PrintWriter(System.out, true); // autoFlush = true
        try {
            Metrics_Output.processGraph("src/main/java/graph/data/small_graphs.json", pw);
        } catch (Exception e) {
            fail("Failed to process graph: " + e.getMessage());
        }
    }


}
