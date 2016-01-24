package ru.ilyaeremin.vkdialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class AvatarDrawable extends Drawable {

    private Bitmap[] bitmaps = new Bitmap[4];

    private int size;

    public static int width  = AndroidUtils.dp(56);
    public static int height = AndroidUtils.dp(56);

    Bitmap bitmapPlaceholder = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    private int left;
    private int top;
    final Rect rect = new Rect(0, 0, width, height);

    final Paint bitmapPaint = new Paint();

    public AvatarDrawable() {
        resetDrawables();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setColor(Color.WHITE);
        bitmapPlaceholder.eraseColor(0xFFEEEEEE);
        setBounds(0, 0, width, height);
    }

    private void resetDrawables() {
        for (int i = 0; i < 4; i++) {
            bitmaps[i] = bitmapPlaceholder;
        }
    }

    final Paint paint = new Paint();
    private Bitmap bitmap;

    @Override
    public void draw(Canvas canvas) {
        Canvas myCanvas;
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(getIntrinsicWidth(), getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            myCanvas = new Canvas(bitmap);
        } else {
            myCanvas = new Canvas(bitmap);
            myCanvas.drawColor(Color.WHITE);
        }

        if (size == 1) {
            myCanvas.drawBitmap(bitmaps[0], null, rect, paint);
            makeCircle(canvas, bitmap);
            return;
        }
        int width = getBounds().width();
        int height = getBounds().height();

        myCanvas.save();
        if (size == 2 || size == 3) {
            // Paint left half
            myCanvas.save();
            myCanvas.clipRect(0, 0, width / 2 - AndroidUtils.dp(1), height);
            myCanvas.translate(-width / 4, 0);
            myCanvas.drawBitmap(bitmaps[0], null, rect, paint);
            myCanvas.restore();
        }
        if (size == 2) {
            // Paint right half
            myCanvas.save();
            myCanvas.clipRect(width / 2 + AndroidUtils.dp(1), 0, width, height);
            myCanvas.translate(width / 4, 0);
            myCanvas.drawBitmap(bitmaps[1], null, rect, paint);
            myCanvas.restore();
        } else {
            // Paint top right
            myCanvas.save();
            myCanvas.scale(.5f, .5f);
            myCanvas.translate(width + AndroidUtils.dp(2), -AndroidUtils.dp(4));
            myCanvas.drawBitmap(bitmaps[1], null, rect, paint);

            // Paint bottom right
            myCanvas.translate(0, height + AndroidUtils.dp(4));
            myCanvas.drawBitmap(bitmaps[2], null, rect, paint);
            myCanvas.restore();
        }

        if (size == 4) {
            // Paint top left
            myCanvas.save();
            myCanvas.scale(.5f, .5f);
            myCanvas.translate(-AndroidUtils.dp(2), -AndroidUtils.dp(4));
            myCanvas.drawBitmap(bitmaps[0], null, rect, paint);

            // Paint bottom left
            myCanvas.translate(0, height + AndroidUtils.dp(4));
            myCanvas.drawBitmap(bitmaps[3], null, rect, paint);
            myCanvas.restore();
        }

        myCanvas.restore();
        makeCircle(canvas, bitmap);
    }

    private final RectF mBorderRect = new RectF();

    private void makeCircle(Canvas canvas, Bitmap bitmap) {
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapHeight = bitmap.getHeight();
        mBitmapWidth = bitmap.getWidth();
        mBorderRect.set(0, 0, getIntrinsicWidth(), getIntrinsicHeight());
        mDrawableRect.set(mBorderRect);

        updateShaderMatrix();

        bitmapPaint.setShader(mBitmapShader);
        canvas.save();
        canvas.translate(left, top);
        canvas.clipRect(0, 0, width, height);
        canvas.drawCircle(width / 2, height / 2, width / 2, bitmapPaint);
        canvas.restore();
    }


    private final Matrix mShaderMatrix = new Matrix();
    private BitmapShader mBitmapShader;
    private int          mBitmapWidth;
    private int          mBitmapHeight;
    private final RectF mDrawableRect = new RectF();

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setTop(int top) {
        this.top = top;
    }

    @Override public int getIntrinsicHeight() {
        return height;
    }

    @Override public int getIntrinsicWidth() {
        return width;
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
        resetDrawables();
    }

    public void onDownloadFinished(int position, Bitmap bitmap) {
        bitmaps[position] = bitmap;
    }

}