package org.example.graph.scc;

import graph.common.Graph;
import graph.common.Metrics;
import graph.common.Node;
import graph.scc.TarjanSCC;
import org.example.graph.common.GraphUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import graph.scc.SCCResult;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;


class SCCTest {

    @Test
    void testSCCFromJsonArray() {
        JSONArray graphs = GraphUtils.readJsonArray("src/main/java/graph/data/small_graphs.json");
        assertNotNull(graphs);

        for (int i = 0; i < graphs.length(); i++) {
            JSONObject obj = graphs.getJSONObject(i);
            Graph g = GraphUtils.fromJSON(obj);
            Metrics metrics = new Metrics();
            TarjanSCC sccAlg = new TarjanSCC(g, metrics);
            var res = sccAlg.run();

            assertNotNull(res.components);
            assertTrue(res.components.size() > 0);

            System.out.println("Graph #" + i);
            System.out.println("SCC Components: " + res.components);
            System.out.println("Condensation DAG nodes: " + res.condensation.n());
            System.out.println("Metrics: " + metrics);
        }
    }
    @Test
    void testEmptyGraph() {
        Graph g = new Graph(0);
        Metrics m = new Metrics();
        TarjanSCC scc = new TarjanSCC(g, m);
        var res = scc.run();
        assertEquals(0, res.components.size());
    }

    @Test
    void testSingleNode() {
        Graph g = new Graph(1);
        g.setNode(0, new Node(0, "N0", 1));
        Metrics m = new Metrics();
        TarjanSCC scc = new TarjanSCC(g, m);
        var res = scc.run();
        assertEquals(1, res.components.size());
    }

        @Test
        void testSCCSimpleGraph() {
            Graph g = new Graph(4);
            for (int i = 0; i < 4; i++) g.setNode(i, new Node(i, "N" + i, 1));

            g.addEdge(0, 1, 1);
            g.addEdge(1, 2, 1);
            g.addEdge(2, 0, 1); // цикл 0->1->2->0
            g.addEdge(2, 3, 1); // отдельная вершина 3

            Metrics m = new Metrics();
            TarjanSCC scc = new TarjanSCC(g, m);
            SCCResult res = scc.run();

            assertEquals(2, res.components.size(), "Should be 2 SCC");
            assertTrue(res.components.stream().anyMatch(c -> c.containsAll(List.of(0,1,2))));
            assertTrue(res.components.stream().anyMatch(c -> c.contains(3)));
        }
}
