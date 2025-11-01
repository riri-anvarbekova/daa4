package graph.topo;

import graph.common.*;
import java.util.*;
/**
 * I compute a topological order of a DAG using Kahn's algorithm.
 * I also track metrics for pushes and pops.
 */

public class TopologicalSort {
    private final Graph g;
    private final Metrics metrics;
    /**
     * I prepare topological sorting.
     *
     * @param g       the DAG to sort
     * @param metrics metrics collector
     */

    public TopologicalSort(Graph g, Metrics metrics) { this.g = g; this.metrics = metrics; }

    /**
     * I return a topological order of the graph nodes using Kahn's algorithm.
     *
     * @return list of node indices in topological order
     * @throws IllegalStateException if the graph contains a cycle
     */
    public List<Integer> kahnOrder() {
        int n = g.n();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) for (Edge e : g.outEdges(u)) indeg[e.getTo()]++;
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) { q.add(i); metrics.kahnPushes++; }
        List<Integer> order = new ArrayList<>();
        metrics.start();
        while (!q.isEmpty()) {
            int u = q.poll(); metrics.kahnPops++;
            order.add(u);
            for (Edge e : g.outEdges(u)) {
                int v = e.getTo(); indeg[v]--;
                if (indeg[v] == 0) { q.add(v); metrics.kahnPushes++; }
            }
        }
        metrics.stop();
        if (order.size() < n) {
            throw new IllegalStateException("Graph has a cycle; topological order not possible");
        }
        return order;
    }
}
