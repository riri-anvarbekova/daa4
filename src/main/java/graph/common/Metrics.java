package graph.common;

/**
 * I collect metrics about graph algorithms.
 * I track DFS visits and edges, Kahn algorithm pushes and pops, DAG relaxations, and elapsed time.
 */
public class Metrics {
    public long sccDfsVisits = 0;
    public long sccDfsEdges = 0;
    public long kahnPushes = 0;
    public long kahnPops = 0;
    public long dagRelaxations = 0;

    private long startNs = 0;
    private long endNs = 0;

    /** I start the timer for measuring elapsed time. */
    public void start() { startNs = System.nanoTime(); }

    /** I stop the timer for measuring elapsed time. */
    public void stop() { endNs = System.nanoTime(); }

    /** I return the elapsed time in nanoseconds. */
    public long elapsedNs() { return endNs - startNs; }

    /** I reset all my counters and timer. */
    public void reset() {
        sccDfsVisits = sccDfsEdges = kahnPushes = kahnPops = dagRelaxations = 0;
        startNs = endNs = 0;
    }

    @Override
    public String toString() {
        return String.format("Metrics{ sccVisits=%d, sccEdges=%d, kahnPushes=%d, kahnPops=%d, dagRelax=%d, timeNs=%d }",
                sccDfsVisits, sccDfsEdges, kahnPushes, kahnPops, dagRelaxations, elapsedNs());
    }
}
