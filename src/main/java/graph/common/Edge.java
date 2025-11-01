package graph.common;
/**
 * I represent a directed edge in a graph.
 * I store the source vertex, the destination vertex, and my weight.
 */
public class Edge {
    private final int from;
    private final int to;
    private final long weight;

    /**
     * I create a directed edge from one vertex to another with a weight.
     *
     * @param from   the source vertex
     * @param to     the destination vertex
     * @param weight the weight of this edge
     */

    public Edge(int from, int to, long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getFrom() { return from; }
    public int getTo() { return to; }
    public long getWeight() { return weight; }
}