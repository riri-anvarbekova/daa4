package graph.scc;
import graph.common.Graph;

import java.util.List;
/**
 * I store the result of SCC computation.
 * I keep the list of strongly connected components, component IDs for each node, and the condensation DAG.
 */

public final class SCCResult {
    public final List<List<Integer>> components;
    public final int[] compId;
    public final Graph condensation;

    /**
     * I create an SCC result.
     *
     * @param components  list of SCCs
     * @param compId      component ID for each node
     * @param condensation the DAG of SCC components
     */
    public SCCResult(List<List<Integer>> components, int[] compId, Graph condensation) {
        this.components = components;
        this.compId = compId;
        this.condensation = condensation;
    }
}
