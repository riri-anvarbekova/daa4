package graph.common;

public class Metrics {
    public long sccDfsVisits = 0;
    public long sccDfsEdges = 0;
    public long kahnPushes = 0;
    public long kahnPops = 0;
    public long dagRelaxations = 0;


    private long startNs = 0;
    private long endNs = 0;


    public void start() { startNs = System.nanoTime(); }
    public void stop() { endNs = System.nanoTime(); }
    public long elapsedNs() { return endNs - startNs; }


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