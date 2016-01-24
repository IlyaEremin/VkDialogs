package ru.ilyaeremin.vkdialogs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;
import ru.ilyaeremin.vkdialogs.utils.LayoutHelper;

/**
 * Created by Ilya Eremin on 25.01.2016.
 */
public class ProgressView extends FrameLayout {
    public ProgressView(Context context) {
        super(context);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF3f5d81, PorterDuff.Mode.SRC_IN);
        addView(progressBar, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(AndroidUtils.dp(54), View.MeasureSpec.EXACTLY));
    }

}
