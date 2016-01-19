package ru.ilyaeremin.vkdialogs.utils;

import ru.ilyaeremin.vkdialogs.App;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class AndroidUtils {

    public static float density = 1;

    static {
        density = App.applicationContext.getResources().getDisplayMetrics().density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int)Math.ceil(density * value);
    }


}
