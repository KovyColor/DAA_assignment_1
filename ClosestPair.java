package assignment1;

import java.util.Arrays;

public class ClosestPair {
    private final Metrics m;

    public ClosestPair(Metrics m) { this.m = m; }

    public static class Point {
        public final double x,y;
        public Point(double x, double y){ this.x = x; this.y = y; }
    }

    public double closest(Point[] pts) {
        m.startTimer();
        m.enter();
        Point[] byX = pts.clone();
        Arrays.sort(byX, (a,b) -> Double.compare(a.x, b.x));
        Point[] tmp = new Point[pts.length];
        double res = rec(byX, tmp, 0, pts.length);
        m.exit();
        m.stopTimer();
        return res;
    }

    private double rec(Point[] byX, Point[] tmp, int l, int r) {
        m.enter();
        try {
            int n = r - l;
            if (n <= 3) {
                double best = Double.POSITIVE_INFINITY;
                for (int i = l; i < r; ++i)
                    for (int j = i+1; j < r; ++j)
                        best = Math.min(best, dist(byX[i], byX[j]));
                // sort by y for merge step (stable)
                Arrays.sort(byX, l, r, (a,b) -> Double.compare(a.y, b.y));
                return best;
            }
            int mid = (l + r) >>> 1;
            double midx = byX[mid].x;
            double dl = rec(byX, tmp, l, mid);
            double dr = rec(byX, tmp, mid, r);
            double d = Math.min(dl, dr);
            // merge by y into tmp
            int i = l, j = mid, k = l;
            while (i < mid && j < r) {
                if (byX[i].y <= byX[j].y) tmp[k++] = byX[i++];
                else tmp[k++] = byX[j++];
            }
            while (i < mid) tmp[k++] = byX[i++];
            while (j < r) tmp[k++] = byX[j++];
            System.arraycopy(tmp, l, byX, l, n);
            // collect strip
            Point[] strip = new Point[n];
            int sc = 0;
            for (int t = l; t < r; ++t) {
                if (Math.abs(byX[t].x - midx) < d) strip[sc++] = byX[t];
            }
            // check up to 7 neighbors
            for (int p = 0; p < sc; ++p) {
                for (int q = p+1; q < sc && (strip[q].y - strip[p].y) < d; ++q) {
                    d = Math.min(d, dist(strip[p], strip[q]));
                }
            }
            return d;
        } finally {
            m.exit();
        }
    }

    private double dist(Point a, Point b) {
        m.addComparisons(1);
        double dx = a.x - b.x, dy = a.y - b.y;
        return Math.hypot(dx, dy);
    }
}

// Closest Pair of Points in 2D
