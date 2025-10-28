package graph.dagsp;
import java.util.*;


public final class PathResult {
    public final long[] dist;
    public final int[] pred;
    public final Map<Integer, List<Integer>> paths;

    public PathResult(long[] dist, int[] pred, Map<Integer, List<Integer>> paths) {
        this.dist = dist;
        this.pred = pred;
        this.paths = paths;
    }
}
