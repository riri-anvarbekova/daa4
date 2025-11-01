package org.example.graph.common;

import graph.common.Edge;
import graph.common.Graph;
import graph.common.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphUtils {

    public static JSONArray readJsonArray(String path) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            return new JSONArray(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read JSON file: " + path);
        }
    }

    public static Graph fromJSON(JSONObject obj) {
        int n = obj.getInt("n");
        Graph g = new Graph(n);

        for (int i = 0; i < n; i++) {
            g.setNode(i, new Node(i, "Node" + i, 1));
        }

        JSONArray edges = obj.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            int u = e.getInt("u");
            int v = e.getInt("v");
            long w = e.getLong("w");
            g.addEdge(u, v, w);
        }
        return g;
    }
}
