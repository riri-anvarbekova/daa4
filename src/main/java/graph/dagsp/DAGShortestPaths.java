package graph.dagsp;

import graph.common.*;
import java.util.*;
public class DAGShortestPaths {
    private final Graph g;
    private final List<Integer> topo;
    private final Metrics metrics;
    private static final long INF = Long.MAX_VALUE / 4;

    public DAGShortestPaths(Graph g, List<Integer> topo, Metrics metrics) {
        this.g = g;
        this.topo = topo;
        this.metrics = metrics;
    }

    public PathResult shortestFrom(int source) {
        int n = g.n();
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        int[] pred = new int[n];
        Arrays.fill(pred, -1);
        dist[source] = 0;

        for (int u : topo) {
            if (dist[u] == INF) continue;
            for (Edge e : g.getNode(u).getEdges())  {
                metrics.dagRelaxations++;
                int v = e.getTo();
                long w = e.getWeight();
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pred[v] = u;
                }
            }
        }

        Map<Integer, List<Integer>> paths = reconstructPaths(dist, pred);
        return new PathResult(dist, pred, reconstructPaths(dist, pred));
    }

    public PathResult longestFrom(int source) {
        int n = g.n();
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MIN_VALUE / 4);
        int[] pred = new int[n];
        Arrays.fill(pred, -1);
        dist[source] = 0;

        for (int u : topo) {
            if (dist[u] == Long.MIN_VALUE / 4) continue;
            for (Edge e : g.getNode(u).getEdges())  {
                metrics.dagRelaxations++;
                int v = e.getTo();
                long w = e.getWeight();
                if (dist[u] + w > dist[v]) {
                    dist[v] = dist[u] + w;
                    pred[v] = u;
                }
            }
        }

        Map<Integer, List<Integer>> paths = reconstructPaths(dist, pred);
        return new PathResult(dist, pred, reconstructPaths(dist, pred));
    }

    private Map<Integer, List<Integer>> reconstructPaths(long[] dist, int[] pred) {
        Map<Integer, List<Integer>> paths = new HashMap<>();
        for (int v = 0; v < dist.length; v++) {
            if (dist[v] != INF && dist[v] != Long.MIN_VALUE / 4) {
                List<Integer> path = new ArrayList<>();
                for (int x = v; x != -1; x = pred[x]) path.add(x);
                Collections.reverse(path);
                paths.put(v, path);
            }
        }
        return paths;
    }
}
