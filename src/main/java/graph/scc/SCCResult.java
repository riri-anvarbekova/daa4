package graph.scc;
import graph.common.Graph;

import java.util.List;


public final class SCCResult {
    public final List<List<Integer>> components;
    public final int[] compId;
    public final Graph condensation;


    public SCCResult(List<List<Integer>> components, int[] compId, Graph condensation) {
        this.components = components;
        this.compId = compId;
        this.condensation = condensation;
    }
}
