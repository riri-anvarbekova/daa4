package graph.dagsp;
import java.util.*;
/**
 * I store the result of DAG shortest/longest path computations.
 * I keep the distances, predecessors, and reconstructed paths.
 */
public final class PathResult {
    public final long[] dist;
    public final int[] pred;
    public final Map<Integer, List<Integer>> paths;
    /**
     * I create a path result object.
     *
     * @param dist  distances from the source
     * @param pred  predecessor array for path reconstruction
     * @param paths map of reconstructed paths from the source
     */
    public PathResult(long[] dist, int[] pred, Map<Integer, List<Integer>> paths) {
        this.dist = dist;
        this.pred = pred;
        this.paths = paths;
    }
}
