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
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        try {
            processGraph("src/main/java/graph/data/small_graphs.json");
            processGraph("src/main/java/graph/data/medium_graphs.json");
            processGraph("src/main/java/graph/data/large_graphs.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processGraph(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) throw new RuntimeException("File not found: " + filePath);

        String content = new String(Files.readAllBytes(file.toPath()));
        JSONArray graphsArray = new JSONArray(content);

        for (int i = 0; i < graphsArray.length(); i++) {
            JSONObject gObj = graphsArray.getJSONObject(i);

            Graph g = new Graph(gObj.getInt("n"));
            for (int v = 0; v < gObj.getInt("n"); v++) {
                g.setNode(v, new Node(v, "Node" + v, 1));
            }

            JSONArray edges = gObj.getJSONArray("edges");
            for (int j = 0; j < edges.length(); j++) {
                JSONObject e = edges.getJSONObject(j);
                g.addEdge(e.getInt("u"), e.getInt("v"), e.getLong("w"));
            }

            int source = gObj.getInt("source");
            Metrics metrics = new Metrics();

            // SCC
            TarjanSCC sccAlg = new TarjanSCC(g, metrics);
            SCCResult sccRes = sccAlg.run();

            // Topological sort
            TopologicalSort topo = new TopologicalSort(sccRes.condensation, metrics);
            List<Integer> compOrder = topo.kahnOrder();

            // DAG Shortest & Longest Paths
            DAGShortestPaths dagSP = new DAGShortestPaths(sccRes.condensation, compOrder, metrics);
            PathResult shortest = dagSP.shortestFrom(source);
            PathResult longest = dagSP.longestFrom(source);

            // Вывод результатов
            System.out.println("=== Graph #" + i + " ===");
            System.out.println("SCC Components: " + sccRes.components);
            System.out.println("Topological order of components: " + compOrder);
            System.out.print("Derived order of original nodes: ");
            for (int comp : compOrder) {
                for (int node : sccRes.components.get(comp)) System.out.print(node + " ");
            }
            System.out.println();
            System.out.println("Shortest distances: " + Arrays.toString(shortest.dist));
            System.out.println("Longest distances: " + Arrays.toString(longest.dist));
            System.out.println("==============================\n");

            // Сохранение в CSV
            writeResultsToCSV("output.csv", i, sccRes.components, compOrder, shortest.dist, longest.dist);
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

    public static void writeResultsToCSV(String filename, int graphId,
                                         List<List<Integer>> sccComponents,
                                         List<Integer> topoOrder,
                                         long[] shortestDist,
                                         long[] longestDist) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) { // true = дописываем
            pw.println("Graph #" + graphId);
            pw.println("SCC Components: " + sccComponents);
            pw.println("Topological order: " + topoOrder);
            pw.println("Shortest distances: " + Arrays.toString(shortestDist));
            pw.println("Longest distances: " + Arrays.toString(longestDist));
            pw.println(); // пустая строка между графами
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
