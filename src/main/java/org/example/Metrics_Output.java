package org.example;

import graph.common.*;
import graph.dagsp.*;
import graph.scc.TarjanSCC;
import graph.scc.SCCResult;
import graph.topo.TopologicalSort;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Metrics_Output {

    public static void main(String[] args) {
        String[] files = {
                "src/main/java/graph/data/small_graphs.json",
                "src/main/java/graph/data/medium_graphs.json",
                "src/main/java/graph/data/large_graphs.json"
        };

        try (PrintWriter writer = new PrintWriter(new FileWriter("graph_metrics.csv"))) {
            writer.println("File,DFS_Visits,DFS_Edges,DFS_Time_ns,DAGSP_Relaxations,DAGSP_Time_ns,Vertices");

            for (String file : files) {
                processGraph(file, writer);
            }

            System.out.println("CSV file 'graph_metrics.csv' created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Graph buildGraphFromJson(JSONObject obj) {
        int n = obj.getInt("n");
        Graph g = new Graph(n);
        for (int v = 0; v < n; v++) {
            g.setNode(v, new Node(v, "Node" + v, 1));
        }
        JSONArray edges = obj.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            g.addEdge(e.getInt("u"), e.getInt("v"), e.getLong("w"));
        }
        return g;
    }
    public static void dfs(int v, boolean[] visited, Graph g, Metrics metrics) {
        visited[v] = true;
        metrics.sccDfsVisits++;
        for (Edge e : g.outEdges(v)) {
            metrics.sccDfsEdges++;
            if (!visited[e.getTo()]) dfs(e.getTo(), visited, g, metrics);
        }
    }

    public static void processGraph(String filePath, PrintWriter writer) throws Exception {
        String content = Files.readString(Path.of(filePath));
        JSONArray graphsArray = new JSONArray(content);

        for (int i = 0; i < graphsArray.length(); i++) {
            JSONObject gObj = graphsArray.getJSONObject(i);
            Graph g = buildGraphFromJson(gObj);
            int source = gObj.getInt("source");

            Metrics metrics = new Metrics();

            // ===== DFS =====
            metrics.reset();
            long dfsStart = System.nanoTime();
            dfs(0, new boolean[g.n()], g, metrics);
            long dfsTime = System.nanoTime() - dfsStart;
            int dfsVisits = (int) metrics.sccDfsVisits;
            int dfsEdges  = (int) metrics.sccDfsEdges;

            // ===== SCC + Topological Sort + DAGSP =====
            metrics.reset();
            metrics.start();
            TarjanSCC sccAlg = new TarjanSCC(g, metrics);
            SCCResult sccRes = sccAlg.run();

            TopologicalSort topo = new TopologicalSort(sccRes.condensation, metrics);
            List<Integer> compOrder = topo.kahnOrder();

            DAGShortestPaths dagSP = new DAGShortestPaths(sccRes.condensation, compOrder, metrics);
            PathResult shortest = dagSP.shortestFrom(source);
            PathResult longest = dagSP.longestFrom(source);
            metrics.stop();
            int relaxations = (int) metrics.dagRelaxations;
            long dagTime = metrics.elapsedNs();

            writer.printf("%s,%d,%d,%d,%d,%d,%d%n",
                    filePath,
                    dfsVisits,
                    dfsEdges,
                    dfsTime,
                    relaxations,
                    dagTime,
                    g.n()
            );

            System.out.println("=== Graph #" + i + " from " + filePath + " ===");
            System.out.println("DFS Visits: " + dfsVisits + ", DFS Edges: " + dfsEdges + ", DFS Time(ns): " + dfsTime);
            System.out.println("DAGSP Relaxations: " + relaxations + ", DAGSP Time(ns): " + dagTime);
            System.out.println("==============================\n");
        }
    }
}

