package ru.ilyaeremin.vkdialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class MultiDrawable extends Drawable {

    private final Context context;
    private List<Drawable> mDrawables = new ArrayList<>();
    private int size;

    Drawable placeholder = new ColorDrawable(0xFFEEEEEE);

    public MultiDrawable(Context context) {
        this.context = context;
        for (int i = 0; i < 4; i++) {
            mDrawables.add(placeholder);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (size == 1) {
            if (mDrawables.get(0) != null) {
                mDrawables.get(0).draw(canvas);
            }
            return;
        }
        int width = getBounds().width();
        int height = getBounds().height();

        canvas.save();
        canvas.clipRect(0, 0, width, height);

        if (size == 2 || size == 3) {
            // Paint left half
            canvas.save();
            canvas.clipRect(0, 0, width / 2 - AndroidUtils.dp(1), height - AndroidUtils.dp(1));
            canvas.translate(-width / 4, 0);
            if (mDrawables.get(0) != null) {
                mDrawables.get(0).draw(canvas);
            }
            canvas.restore();
        }
        if (size == 2) {
            // Paint right half
            canvas.save();
            canvas.clipRect(width / 2 + AndroidUtils.dp(1), 0, width, height);
            canvas.translate(width / 4, 0);
            if (mDrawables.get(1) != null) {
                mDrawables.get(1).draw(canvas);
            }
            canvas.restore();
        } else {
            // Paint top right
            canvas.save();
            canvas.scale(.5f, .5f);
            canvas.translate(width + AndroidUtils.dp(2), AndroidUtils.dp(2));
            if (mDrawables.get(1) != null) {
                mDrawables.get(1).draw(canvas);
            }

            // Paint bottom right
            canvas.translate(AndroidUtils.dp(2), height + AndroidUtils.dp(2));
            if (mDrawables.get(2) != null) {
                mDrawables.get(2).draw(canvas);
            }
            canvas.restore();
        }

        if (size == 4) {
            // Paint top left
            canvas.save();
            canvas.scale(.5f, .5f);
            canvas.translate(-AndroidUtils.dp(2), -AndroidUtils.dp(2));
            if (mDrawables.get(0) != null) {
                mDrawables.get(0).draw(canvas);
            }

            // Paint bottom left
            canvas.translate(-AndroidUtils.dp(2), height + AndroidUtils.dp(2));
            if (mDrawables.get(3) != null) {
                mDrawables.get(3).draw(canvas);
            }
            canvas.restore();
        }

        canvas.restore();
    }

    @Override public int getIntrinsicHeight() {
        return AndroidUtils.dp(64);
    }

    @Override public int getIntrinsicWidth() {
        return AndroidUtils.dp(64);
    }

    @Override
    public void setAlpha(int i) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void onDownloadFinished(int position, Bitmap bitmap) {
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0, AndroidUtils.dp(64), AndroidUtils.dp(64));
        mDrawables.add(position, drawable);
    }
}