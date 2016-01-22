package ru.ilyaeremin.vkdialogs.utils;

import android.util.Log;

/**
 * Created by Ilya Eremin on 1/22/16.
 */
public class DLogger {
    public static void e(String tag, Exception e) {
        Log.e(tag, "error", e);
    }
}
