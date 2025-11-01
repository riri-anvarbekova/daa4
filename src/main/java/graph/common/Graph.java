package graph.common;

import java.util.*;
/**
 * I represent a directed graph.
 * I store my nodes and the adjacency lists for outgoing and incoming edges.
 */
public class Graph {
    private final List<Node> nodes;
    private final List<List<Edge>> adj;
    private final List<List<Edge>> revAdj;
    /**
     * I create a graph with a given number of nodes.
     *
     * @param n the number of nodes I will have
     */
    public Graph(int n) {
        nodes = new ArrayList<>();
        adj = new ArrayList<>();
        revAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) addPlaceholder();
    }

    private void addPlaceholder() {
        nodes.add(null);
        adj.add(new ArrayList<>());
        revAdj.add(new ArrayList<>());
    }

    public void setNode(int idx, Node node) {
        nodes.set(idx, node);
    }
    /**
     * I add a directed edge from one node to another with a weight.
     *
     * @param from   source node index
     * @param to     destination node index
     * @param weight the weight of the edge
     */
    public void addEdge(int from, int to, long weight) {
        Edge e = new Edge(from, to, weight);
        adj.get(from).add(e);
        revAdj.get(to).add(e);
        Node nf = nodes.get(from);
        if (nf != null) nf.addEdge(e);
    }

    public int n() { return nodes.size(); }
    public int m() {
        int total = 0;
        for (List<Edge> list : adj) total += list.size();
        return total;
    }

    public List<Edge> outEdges(int u) { return Collections.unmodifiableList(adj.get(u)); }
    public List<Edge> inEdges(int u) { return Collections.unmodifiableList(revAdj.get(u)); }

    public Node getNode(int u) { return nodes.get(u); }

    public static Graph fromNodes(List<Node> nodeList) {
        Graph g = new Graph(nodeList.size());
        for (int i = 0; i < nodeList.size(); i++) g.setNode(i, nodeList.get(i));
        return g;
    }
}
