package assignment1;

import java.util.Random;

public class QuickSort {
    private final Metrics m;
    private final Random rnd = new Random();

    public QuickSort(Metrics m) { this.m = m; }

    public void sort(int[] a) {
        m.startTimer();
        m.enter();
        quicksort(a, 0, a.length - 1);
        m.exit();
        m.stopTimer();
    }

    private void quicksort(int[] a, int lo, int hi) {
        int startLo = lo, startHi = hi;
        while (lo < hi) {
            m.enter();
            try {
                int n = hi - lo + 1;
                if (n <= 16) {
                    insertion(a, lo, hi);
                    return;
                }
                int pivotIndex = lo + rnd.nextInt(n);
                int pivot = a[pivotIndex];
                // 3-way partition to handle duplicates
                int i = lo, lt = lo, gt = hi;
                while (i <= gt) {
                    m.addComparisons(1);
                    if (a[i] < pivot) { swap(a, lt++, i++); }
                    else if (a[i] > pivot) { swap(a, i, gt--); }
                    else i++;
                }
                // recurse on smaller side, iterate on larger
                int leftSize = lt - lo;
                int rightSize = hi - gt;
                if (leftSize < rightSize) {
                    quicksort(a, lo, lt - 1);
                    lo = gt + 1; // iterate on right
                } else {
                    quicksort(a, gt + 1, hi);
                    hi = lt - 1;
                }
            } finally {
                m.exit();
            }
        }
    }

    private void insertion(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            int key = a[i]; int j = i - 1;
            while (j >= lo) {
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

// QuickSort implementation with randomized pivot
