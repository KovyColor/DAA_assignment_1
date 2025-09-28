package assignment1;

import assignment1.ClosestPair.Point;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        // Simple CLI: args = [algorithm] [n] [csvPath]
        // algorithm: mergesort | quicksort | select | closest
        String alg = args.length > 0 ? args[0] : "mergesort";
        int n = args.length > 1 ? Integer.parseInt(args[1]) : 100_000;
        String csv = args.length > 2 ? args[2] : "metrics.csv";

        Metrics.writeCsvHeader(csv);

        Metrics m = new Metrics();

        switch (alg.toLowerCase()) {
            case "mergesort":
                int[] a1 = randomIntArray(n);
                MergeSort ms = new MergeSort(m);
                ms.sort(a1);
                System.out.println("sorted? " + SortUtils.isSorted(a1));
                m.appendCsv(csv, "MergeSort", n);
                break;

            case "quicksort":
                int[] a2 = randomIntArray(n);
                SortUtils.shuffle(a2); // make adversarial less likely
                QuickSort qs = new QuickSort(m);
                qs.sort(a2);
                System.out.println("sorted? " + SortUtils.isSorted(a2));
                m.appendCsv(csv, "QuickSort", n);
                break;

            case "select":
                int[] a3 = randomIntArray(n);
                int k = n/2;
                DeterministicSelect ds = new DeterministicSelect(m);
                int kth = ds.select(a3.clone(), k);
                Arrays.sort(a3);
                System.out.println("select ok? " + (kth == a3[k]));
                m.appendCsv(csv, "DeterministicSelect", n);
                break;

            case "closest":
                Point[] pts = randomPoints(n);
                ClosestPair cp = new ClosestPair(m);
                double d = cp.closest(pts);
                System.out.println("closest distance: " + d);
                m.appendCsv(csv, "ClosestPair", n);
                break;

            default:
                System.err.println("Unknown alg: " + alg);
                System.exit(1);
        }

        System.out.println("Elapsed (ms): " + (m.elapsedNs / 1_000_000));
        System.out.println("Max depth: " + m.maxDepth);
        System.out.println("Comparisons: " + m.comparisons.get());
        System.out.println("Allocations (approx bytes): " + m.allocations.get());
        System.out.println("CSV saved to " + csv);
    }

    private static int[] randomIntArray(int n) {
        Random r = new Random(12345);
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) a[i] = r.nextInt();
        return a;
    }

    private static Point[] randomPoints(int n) {
        Random r = new Random(123);
        Point[] pts = new Point[n];
        for (int i = 0; i < n; ++i) pts[i] = new Point(r.nextDouble()*1000, r.nextDouble()*1000);
        return pts;
    }
}
