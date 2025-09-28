package assignment1;

public class DeterministicSelect {
    private final Metrics m;

    public DeterministicSelect(Metrics m) { this.m = m; }
    
    public int select(int[] a, int k) {
        m.startTimer();
        m.enter();
        int res = selectInplace(a, 0, a.length - 1, k);
        m.exit();
        m.stopTimer();
        return res;
    }

    private int selectInplace(int[] a, int l, int r, int k) {
        m.enter();
        try {
            while (true) {
                if (l == r) return a[l];
                int pivotIndex = medianOfMedians(a, l, r);
                pivotIndex = partition(a, l, r, pivotIndex);
                int rank = pivotIndex - l;
                if (k == rank) return a[pivotIndex];
                else if (k < rank) r = pivotIndex - 1;
                else { k = k - rank - 1; l = pivotIndex + 1; }
            }
        } finally {
            m.exit();
        }
    }

    private int medianOfMedians(int[] a, int l, int r) {
        int n = r - l + 1;
        if (n <= 5) {
            insertionSort(a, l, r);
            return l + n/2;
        }
        int numMedians = 0;
        for (int i = l; i <= r; i += 5) {
            int subR = Math.min(i+4, r);
            insertionSort(a, i, subR);
            int medianIdx = i + (subR - i)/2;
            swap(a, l + numMedians, medianIdx);
            numMedians++;
        }
        return selectInplace(a, l, l + numMedians - 1, numMedians / 2);
    }

    private int partition(int[] a, int l, int r, int pivotIndex) {
        int pivot = a[pivotIndex];
        swap(a, pivotIndex, r);
        int store = l;
        for (int i = l; i < r; ++i) {
            m.addComparisons(1);
            if (a[i] < pivot) { swap(a, store, i); store++; }
        }
        swap(a, store, r);
        return store;
    }

    private void insertionSort(int[] a, int l, int r) {
        for (int i = l + 1; i <= r; ++i) {
            int key = a[i]; int j = i - 1;
            while (j >= l) {
                m.addComparisons(1);
                if (a[j] > key) { a[j+1] = a[j]; j--; }
                else break;
            }
            a[j+1] = key;
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}

// Deterministic Select (Median-of-Medians)
