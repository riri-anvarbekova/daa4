package graph.dagsp;

import graph.common.*;
import java.util.*;

/**
 * I compute shortest and longest paths in a DAG given a topological order.
 * Longest paths are computed using node durations to avoid negative results.
 */
public class DAGShortestPaths {
    private final Graph g;
    private final List<Integer> topo;
    private final Metrics metrics;
    private static final long INF = Long.MAX_VALUE / 4;
    private static final long NEG_INF = Long.MIN_VALUE / 4;

    /**
     * I prepare DAG shortest/longest path computations.
     *
     * @param g       the DAG
     * @param topo    the topological order of nodes
     * @param metrics metrics collector
     */
    public DAGShortestPaths(Graph g, List<Integer> topo, Metrics metrics) {
        this.g = g;
        this.topo = topo;
        this.metrics = metrics;
    }

    /**
     * Compute single-source shortest paths in a DAG.
     *
     * @param source the source node index
     * @return PathResult with distances, predecessors, and paths
     */
    public PathResult shortestFrom(int source) {
        int n = g.n();
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        int[] pred = new int[n];
        Arrays.fill(pred, -1);

        dist[source] = 0;

        for (int u : topo) {
            if (dist[u] == INF) continue;
            for (Edge e : g.outEdges(u)) {
                metrics.dagRelaxations++;
                int v = e.getTo();
                long w = e.getWeight();
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pred[v] = u;
                }
            }
        }

        return new PathResult(dist, pred, reconstructPaths(dist, pred, INF));
    }

    /**
     * Compute single-source longest paths in a DAG.
     * Uses node durations to ensure values do not go negative.
     *
     * @param source the source node index
     * @return PathResult with distances, predecessors, and paths
     */
    public PathResult longestFrom(int source) {
        int n = g.n();
        long[] dist = new long[n];
        Arrays.fill(dist, NEG_INF);
        int[] pred = new int[n];
        Arrays.fill(pred, -1);

        dist[source] = g.getNode(source).getDuration(); // стартуем с duration источника

        for (int u : topo) {
            if (dist[u] == NEG_INF) continue;
            for (Edge e : g.outEdges(u)) {
                metrics.dagRelaxations++;
                int v = e.getTo();
                long w = e.getWeight();
                long potential = dist[u] + w + g.getNode(v).getDuration(); // учитываем duration вершины v
                if (potential > dist[v]) {
                    dist[v] = potential;
                    pred[v] = u;
                }
            }
        }

        return new PathResult(dist, pred, reconstructPathsForLongest(dist, pred, NEG_INF));
    }

    private Map<Integer, List<Integer>> reconstructPaths(long[] dist, int[] pred, long INF_VAL) {
        Map<Integer, List<Integer>> paths = new HashMap<>();
        for (int v = 0; v < dist.length; v++) {
            if (dist[v] != INF_VAL) {
                List<Integer> path = new ArrayList<>();
                for (int x = v; x != -1; x = pred[x]) path.add(x);
                Collections.reverse(path);
                paths.put(v, path);
            }
        }
        return paths;
    }

    private Map<Integer, List<Integer>> reconstructPathsForLongest(long[] dist, int[] pred, long NEG_INF_VAL) {
        Map<Integer, List<Integer>> paths = new HashMap<>();
        for (int v = 0; v < dist.length; v++) {
            if (dist[v] != NEG_INF_VAL) {
                List<Integer> path = new ArrayList<>();
                for (int x = v; x != -1; x = pred[x]) path.add(x);
                Collections.reverse(path);
                paths.put(v, path);
            }
        }
        return paths;
    }
}
