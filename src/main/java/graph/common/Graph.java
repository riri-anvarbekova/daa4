package graph.common;

import java.util.*;


public class Graph {
    private final List<Node> nodes;
    private final List<List<Edge>> adj;
    private final List<List<Edge>> revAdj;


    public Graph(int n) {
        nodes = new ArrayList<>();
        adj = new ArrayList<>();
        revAdj = new ArrayList<>();
        for (int i = 0; i < n; i++) { addPlaceholder(); }
    }


    private void addPlaceholder(){
        nodes.add(null);
        adj.add(new ArrayList<>());
        revAdj.add(new ArrayList<>());
    }


    public void setNode(int idx, Node node) {
        nodes.set(idx, node);
    }


    public void addEdge(int from, int to, long weight) {
        Edge e = new Edge(from, to, weight);
        adj.get(from).add(e);
        revAdj.get(to).add(e);
    }


    public int n() { return nodes.size(); }
    public List<Edge> outEdges(int u) { return Collections.unmodifiableList(adj.get(u)); }
    public List<Edge> inEdges(int u) { return Collections.unmodifiableList(revAdj.get(u)); }


    public Node getNode(int u) { return nodes.get(u); }


    public static Graph fromNodes(List<Node> nodeList) {
        Graph g = new Graph(nodeList.size());
        for (int i = 0; i < nodeList.size(); i++) g.setNode(i, nodeList.get(i));
        return g;
    }
}