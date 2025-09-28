package assignment1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

public class Metrics {
    public long startTimeNs;
    public long elapsedNs;
    public AtomicLong comparisons = new AtomicLong(0);
    public AtomicLong allocations = new AtomicLong(0);
    public int maxDepth = 0;
    private ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    public void startTimer() { startTimeNs = System.nanoTime(); }
    public void stopTimer() { elapsedNs = System.nanoTime() - startTimeNs; }

    public void enter() {
        int d = depth.get() + 1;
        depth.set(d);
        if (d > maxDepth) maxDepth = d;
    }

    public void exit() {
        depth.set(Math.max(0, depth.get() - 1));
    }

    public void addComparisons(long x) { comparisons.addAndGet(x); }
    public void addAlloc(long x) { allocations.addAndGet(x); }

    public String toCsv(String algorithm, int n) {
        return String.format("%s,%d,%d,%d,%d,%d", algorithm, n, elapsedNs, maxDepth, comparisons.get(), allocations.get());
    }

    public static void writeCsvHeader(String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, false))) {
            pw.println("algorithm,n,elapsed_ns,max_depth,comparisons,allocations");
        }
    }

    public void appendCsv(String path, String algorithm, int n) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) {
            pw.println(toCsv(algorithm, n));
        }
    }
}
