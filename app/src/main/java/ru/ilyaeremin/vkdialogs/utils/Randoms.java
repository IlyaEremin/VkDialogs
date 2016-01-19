package ru.ilyaeremin.vkdialogs.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class Randoms {

    public static int[] getNRandomNumberWithinRange(int n, int range) {
        int[] randoms = new int[range];
        for (int i = 0; i < range; i++) {
            randoms[i] = i;
        }
        shuffleArray(randoms);
        return Arrays.copyOf(randoms, n);
    }

    // Implementing Fisher–Yates shuffle
    public static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
