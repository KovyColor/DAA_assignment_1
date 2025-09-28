package assignment1;

import java.util.Random;

public class SortUtils {
    private static final Random rnd = new Random();

    public static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    public static void shuffle(int[] a) {
        for (int i = a.length - 1; i > 0; --i) {
            int j = rnd.nextInt(i + 1);
            swap(a, i, j);
        }
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; ++i)
            if (a[i-1] > a[i]) return false;
        return true;
    }
}
