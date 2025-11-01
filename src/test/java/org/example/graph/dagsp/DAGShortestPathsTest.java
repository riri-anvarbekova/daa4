package org.example.graph.dagsp;

import graph.common.Graph;
import graph.common.Metrics;
import graph.common.Node;
import graph.dagsp.DAGShortestPaths;
import graph.dagsp.PathResult;
import graph.topo.TopologicalSort;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileInputStream;


class DAGShortestPathsTest {
    //Tests Shortest and Longest Paths on JSON-loaded graphs
    @Test
    void testDAGShortestAndLongestPathsFromJson() {
        try (FileInputStream fis = new FileInputStream("src/main/java/graph/data/small_graphs.json")) {
            JSONArray graphsArray = new JSONArray(new String(fis.readAllBytes()));

            for (int i = 0; i < graphsArray.length(); i++) {
                JSONObject gObj = graphsArray.getJSONObject(i);
                Graph g = new Graph(gObj.getInt("n"));

                for (int v = 0; v < gObj.getInt("n"); v++) {
                    g.setNode(v, new graph.common.Node(v, "Node" + v, 1));
                }

                JSONArray edges = gObj.getJSONArray("edges");
                for (int j = 0; j < edges.length(); j++) {
                    JSONObject e = edges.getJSONObject(j);
                    g.addEdge(e.getInt("u"), e.getInt("v"), e.getLong("w"));
                }

                Metrics metrics = new Metrics();
                List<Integer> topo = new TopologicalSort(g, metrics).kahnOrder();
                DAGShortestPaths dagSP = new DAGShortestPaths(g, topo, metrics);

                int source = gObj.getInt("source");
                var shortest = dagSP.shortestFrom(source);
                var longest = dagSP.longestFrom(source);

                System.out.println("Graph #" + i);
                System.out.println("Shortest distances from " + source + ":");
                for (long d : shortest.dist) System.out.print(d + " ");
                System.out.println("\nLongest distances from " + source + ":");
                for (long d : longest.dist) System.out.print(d + " ");
                System.out.println("\nMetrics: " + metrics);

                assertNotNull(shortest.paths.get(source));
                assertNotNull(longest.paths.get(source));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to read or process graphs JSON");
        }
    }
    //Tests Shortest Path on a minimal graph
    @Test
    void testSingleNodeDAGSP() {
        Graph g = new Graph(1);
        g.setNode(0, new Node(0, "N0", 1));
        Metrics m = new Metrics();
        List<Integer> topo = List.of(0);
        DAGShortestPaths dagSP = new DAGShortestPaths(g, topo, m);
        var sp = dagSP.shortestFrom(0);
        assertEquals(0, sp.dist[0]);
    }
    //Tests Shortest/Longest Paths on a simple, known 4-node DAG. Verifies expected distances.
    @Test
    void testDAGShortestAndLongestPaths() {
        Graph g = new Graph(4);
        for (int i = 0; i < 4; i++) g.setNode(i, new Node(i, "N" + i, 1));

        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);

        Metrics m = new Metrics();
        List<Integer> topo = new TopologicalSort(g, m).kahnOrder();

        DAGShortestPaths dagSP = new DAGShortestPaths(g, topo, m);
        PathResult shortest = dagSP.shortestFrom(0);
        PathResult longest = dagSP.longestFrom(0);

        assertEquals(0, shortest.dist[0]);
        assertEquals(1, shortest.dist[1]);
        assertEquals(1, shortest.dist[2]);
        assertEquals(2, shortest.dist[3]);

        assertEquals(0, longest.dist[0]);
        assertEquals(1, longest.dist[1]);
        assertEquals(1, longest.dist[2]);
        assertEquals(2, longest.dist[3]);
    }
}
