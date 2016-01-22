package ru.ilyaeremin.vkdialogs.utils;

import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

import ru.ilyaeremin.vkdialogs.App;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class AndroidUtils {

    public static        float                       density       = 1;
    public static        int                         leftBaseline  = 72;
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();

    static {
        density = App.applicationContext.getResources().getDisplayMetrics().density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(App.applicationContext.getAssets(), assetPath);
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e("Typefaces", "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }


}
