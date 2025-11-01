package graph.common;

import java.util.*;
/**
 * I represent a node in the graph.
 * I hold my ID, label, duration, and a list of outgoing edges.
 */
public final class Node {
    private final int id;
    private final String label;
    private final long duration;
    public final List<Edge> edges = new ArrayList<>();
    /**
     * I create a node with an ID, label, and duration.
     *
     * @param id       my identifier
     * @param label    my label or name
     * @param duration my duration or weight
     */
    public Node(int id, String label, long duration) {
        this.id = id;
        this.label = label;
        this.duration = duration;
    }

    public int getId() { return id; }
    public String getLabel() { return label; }
    public long getDuration() { return duration; }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }
}
