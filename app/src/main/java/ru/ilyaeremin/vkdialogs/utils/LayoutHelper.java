package ru.ilyaeremin.vkdialogs.utils;

import android.widget.FrameLayout;

/**
 * Created by Ilya Eremin on 25.01.2016.
 */
public class LayoutHelper {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    public static FrameLayout.LayoutParams createFrame(int width, int height, int gravity) {
        return new FrameLayout.LayoutParams(getSize(width), getSize(height), gravity);
    }

    private static int getSize(float size) {
        return (int) (size < 0 ? size : AndroidUtils.dp(size));
    }
}
