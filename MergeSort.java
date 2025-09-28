package assignment1;

public class MergeSort {
    private final Metrics m;

    public MergeSort(Metrics m) { this.m = m; }

    public void sort(int[] a) {
        m.startTimer();
        m.enter();
        int[] buf = new int[a.length];
        m.addAlloc(a.length * 4L); // rough bytes estimate
        mergesort(a, buf, 0, a.length);
        m.exit();
        m.stopTimer();
    }

    private void mergesort(int[] a, int[] buf, int l, int r) {
        m.enter();
        try {
            int n = r - l;
            if (n <= 32) { // cutoff to insertion sort
                insertion(a, l, r);
                return;
            }
            int mid = (l + r) >>> 1;
            mergesort(a, buf, l, mid);
            mergesort(a, buf, mid, r);
            // merge
            int i = l, j = mid, k = l;
            while (i < mid && j < r) {
                m.addComparisons(1);
                if (a[i] <= a[j]) buf[k++] = a[i++];
                else buf[k++] = a[j++];
            }
            while (i < mid) buf[k++] = a[i++];
            while (j < r) buf[k++] = a[j++];
            System.arraycopy(buf, l, a, l, n);
        } finally {
            m.exit();
        }
    }

    private void insertion(int[] a, int l, int r) {
        for (int i = l + 1; i < r; ++i) {
            int key = a[i];
            int j = i - 1;
            while (j >= l) {
                m.addComparisons(1);
                if (a[j] > key) { a[j+1] = a[j]; j--; }
                else break;
            }
            a[j+1] = key;
        }
    }
}

// MergeSort implementation
