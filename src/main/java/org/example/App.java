package org.example;

import graph.common.*;
import graph.scc.*;
import graph.topo.TopologicalSort;
import graph.dagsp.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class App {

    public static Graph buildGraphFromJson(JSONObject obj) {
        int n = obj.getInt("n");
        Graph g = new Graph(n);
        for (int i = 0; i < n; i++) g.setNode(i, new Node(i, "Node" + i, 1));

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

    public static void processGraph(Graph g, int index, String filename) {
        System.out.println("---------------------------------------------------");
        System.out.println("📊 Graph " + index + " from " + filename);
        System.out.println("Nodes: " + g.n());
        System.out.println("Edges: " + g.m());

        Metrics metrics = new Metrics();

        TarjanSCC sccAlg = new TarjanSCC(g, metrics);
        SCCResult sccRes = sccAlg.run();

        System.out.println("🔹 Strongly Connected Components:");
        for (int i = 0; i < sccRes.components.size(); i++) {
            System.out.println("  Component " + i + ": " + sccRes.components.get(i));
        }

        Graph dag = sccRes.condensation;
        System.out.println("Condensation DAG: " + dag.n() + " nodes, " + dag.m() + " edges");

        TopologicalSort topoAlg = new TopologicalSort(dag, metrics);
        List<Integer> topoOrder = topoAlg.kahnOrder();
        System.out.println("🌀 Topological Order of components: " + topoOrder);

        int sourceComp = 0;
        DAGShortestPaths dagSP = new DAGShortestPaths(dag, topoOrder, metrics);

        PathResult shortest = dagSP.shortestFrom(sourceComp);
        System.out.println("🚀 Shortest distances from " + sourceComp + ": " + Arrays.toString(shortest.dist));

        PathResult longest = dagSP.longestFrom(sourceComp);
        System.out.println("🔥 Longest distances from " + sourceComp + ": " + Arrays.toString(longest.dist));

        System.out.println("Metrics: " + metrics);
        System.out.println("---------------------------------------------------");
    }
}
