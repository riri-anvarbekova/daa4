package org.example.graph.dagsp;

import graph.common.*;
import graph.topo.TopologicalSort;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TopologicalSortTest {

    @Test
    void testTopologicalOrder() {
        try (FileInputStream fis = new FileInputStream("src/main/java/graph/data/small_graphs.json")) {
            JSONArray graphsArray = new JSONArray(new String(fis.readAllBytes()));

            for (int i = 0; i < graphsArray.length(); i++) {
                JSONObject gObj = graphsArray.getJSONObject(i);
                Graph g = new Graph(gObj.getInt("n"));

                // Добавляем вершины
                for (int v = 0; v < gObj.getInt("n"); v++) {
                    g.setNode(v, new Node(v, "Node" + v, 1));
                }

                // Добавляем ребра
                JSONArray edges = gObj.getJSONArray("edges");
                for (int j = 0; j < edges.length(); j++) {
                    JSONObject e = edges.getJSONObject(j);
                    g.addEdge(e.getInt("u"), e.getInt("v"), e.getLong("w"));
                }

                Metrics metrics = new Metrics();
                TopologicalSort topo = new TopologicalSort(g, metrics);
                List<Integer> order = topo.kahnOrder();

                assertEquals(g.n(), order.size(), "Topological order size mismatch");

                for (int u : order) {
                    for (var e : g.outEdges(u)) {
                        int v = e.getTo();
                        assertTrue(order.indexOf(u) < order.indexOf(v), "Topological order violated");
                    }
                }

                System.out.println("Graph #" + i + " Topological order: " + order);
                System.out.println("Metrics: " + metrics);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to read or process graphs JSON");
        }
    }
    @Test
    void testEmptyGraphTopo() {
        Graph g = new Graph(0);
        Metrics m = new Metrics();
        TopologicalSort topo = new TopologicalSort(g, m);
        List<Integer> order = topo.kahnOrder();
        assertTrue(order.isEmpty());
    }

    @Test
    void testSingleNodeTopo() {
        Graph g = new Graph(1);
        g.setNode(0, new Node(0, "N0", 1));
        Metrics m = new Metrics();
        TopologicalSort topo = new TopologicalSort(g, m);
        List<Integer> order = topo.kahnOrder();
        assertEquals(1, order.size());
    }

}
