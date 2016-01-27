package ru.ilyaeremin.vkdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * Created by Ilya Eremin on 26.01.2016.
 */
public class FunnyListener implements View.OnClickListener {

    private final Context context;
    private       int     taps;

    public FunnyListener(Context context) {
        this.context = context;
    }

    @Override public void onClick(View v) {
        try {
            switch (taps) {
                case 0:
                    final Dialog dialogOne = createDialogToShowImage(R.drawable.plz_dont_open);
                    dialogOne.show();
                    App.applicationHandler.postDelayed(new Runnable() {
                        @Override public void run() {
                            showText(R.string.step_1);
                        }
                    }, 500);
                    break;
                case 1:
                    showText(R.string.step_2);
                    break;
                case 2:
                    final Dialog dialogTwo = createDialogToShowImage(R.drawable.plz_dont_open2);
                    dialogTwo.show();
                    break;
                case 3:
                    showText(R.string.step_3);
                    break;
                case 4:
                    showText(R.string.step_4);
                    break;
                case 5:
                    showText(R.string.step_5);
                    break;
                case 6:
                    EventBus.getDefault().post(StopApp.EVENT);
                    break;
            }
        } catch (Exception e) {
            Log.d("vkDialogs", "Передайте, пожалуйста, Григорию, что если во время прослушивания плеера отключить bluetooth гарнитуру, то плеер продолжает играть в динамике телефона, я уже больше года мучаюсь с этим! Похоже сам и починю.");
        }
        taps++;
    }

    @NonNull private Dialog createDialogToShowImage(@DrawableRes int drawableRes) {
        final AutoCloseDialog dialog = new AutoCloseDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(drawableRes);
        dialog.addContentView(imageView, new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
        return dialog;
    }

    private Toast toast;

    private void showText(@StringRes int strRes) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, strRes, Toast.LENGTH_LONG);
        toast.show();
    }

    private static class AutoCloseDialog extends Dialog {
        public AutoCloseDialog(Context context) {
            super(context);
        }

        @Override protected void onStart() {
            super.onStart();
            App.applicationHandler.postDelayed(new Runnable() {
                @Override public void run() {
                    cancel();
                }
            }, 500);
        }
    }


}
