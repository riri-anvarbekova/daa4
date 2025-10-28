package graph.scc;
import graph.common.*;
import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;
    private int n;
    private int index = 0;
    private int[] idx;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private final List<List<Integer>> components = new ArrayList<>();

    public TarjanSCC(Graph g, Metrics metrics) {
        this.g = g; this.metrics = metrics; this.n = g.n();
        idx = new int[n]; Arrays.fill(idx, -1);
        low = new int[n]; onStack = new boolean[n]; stack = new ArrayDeque<>();
    }

    public SCCResult run() {
        metrics.start();
        for (int v = 0; v < n; v++) {
            if (idx[v] == -1) strongconnect(v);
        }
        metrics.stop();
        // build compId
        int k = components.size();
        int[] compId = new int[n];
        for (int i = 0; i < k; i++) {
            for (int v : components.get(i)) compId[v] = i;
        }
        Graph cond = new Graph(k);
        for (int i = 0; i < k; i++) {
            cond.setNode(i, new Node(i, "comp"+i, components.get(i).stream().mapToLong(v->g.getNode(v).getDuration()).sum()));
        }
        for (int u = 0; u < n; u++) {
            for (Edge e : g.outEdges(u)) {
                int cu = compId[u], cv = compId[e.getTo()];
                if (cu != cv) {
                    boolean exists = cond.outEdges(cu).stream().anyMatch(ed->ed.getTo()==cv);
                    if (!exists) cond.addEdge(cu, cv, 0L);
                }
            }
        }
        return new SCCResult(components, compId, cond);
    }

    private void strongconnect(int v) {
        idx[v] = index; low[v] = index; index++;
        stack.push(v); onStack[v] = true; metrics.sccDfsVisits++;
        for (Edge e : g.outEdges(v)) {
            int w = e.getTo(); metrics.sccDfsEdges++;
            if (idx[w] == -1) {
                strongconnect(w);
                low[v] = Math.min(low[v], low[w]);
            } else if (onStack[w]) {
                low[v] = Math.min(low[v], idx[w]);
            }
        }
        if (low[v] == idx[v]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop(); onStack[w] = false; comp.add(w);
                if (w == v) break;
            }
            components.add(comp);
        }
    }
}
