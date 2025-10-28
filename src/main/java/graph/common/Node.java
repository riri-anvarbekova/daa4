package graph.common;

import java.util.*;

public final class Node {
    private final int id;
    private final String label;
    private final long duration;
    private final List<Edge> edges = new ArrayList<>(); // приватное поле

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
