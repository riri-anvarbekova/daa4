package org.example.graph.scc;

import graph.common.Graph;
import graph.common.Metrics;
import graph.common.Node;
import graph.scc.TarjanSCC;
import org.example.graph.common.GraphUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

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
}
