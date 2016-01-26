package ru.ilyaeremin.vkdialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import ru.ilyaeremin.vkdialogs.models.Dialog;
import ru.ilyaeremin.vkdialogs.utils.AndroidUtils;
import ru.ilyaeremin.vkdialogs.utils.DLogger;

/**
 * Created by Ilya Eremin on 1/22/16.
 */
public class DialogView extends View {

    private static TextPaint namePaint;
    private static TextPaint messagePaint;
    private static TextPaint timePaint;

    private int chatNameLeft;
    private int chatNameTop = AndroidUtils.dp(20);
    private StaticLayout chatNameLayout;

    private int timeTop = AndroidUtils.dp(20);
    private int          timeLeft;
    private StaticLayout timeLayout;

    private int messageTop = AndroidUtils.dp(40);
    private int          messageLeft;
    private StaticLayout messageLayout;

    private Dialog         dialog;
    private AvatarDrawable avatar;

    public DialogView(Context context) {
        super(context);

        namePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        namePaint.setTextSize(AndroidUtils.dp(17));
        namePaint.setColor(0xff212121);
        namePaint.setTypeface(AndroidUtils.getTypeface("fonts/rmedium.ttf"));

        messagePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        messagePaint.setTextSize(AndroidUtils.dp(15));
        messagePaint.setColor(0xff8a8a8a);
        messagePaint.linkColor = 0xff8a8a8a;

        timePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        timePaint.setTextSize(AndroidUtils.dp(14));
        timePaint.setColor(0xffa6a6a6);

        avatar = new AvatarDrawable();
        setBackgroundResource(R.drawable.list_selector);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
        update();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtils.dp(74));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (dialog == null) {
            super.onLayout(changed, left, top, right, bottom);
            return;
        }
        if (changed) {
            buildLayout();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                getBackground().setHotspot(event.getX(), event.getY());
            }
        }
        return super.onTouchEvent(event);
    }

    public void buildLayout() {
        String chatNameString = "";
        String timeString = "";
        CharSequence messageString = "";

        TextPaint currentNamePaint = namePaint;
        TextPaint currentMessagePaint = messagePaint;
        boolean checkMessage = true;

        chatNameLeft = AndroidUtils.dp(AndroidUtils.leftBaseline);
        timeString = dialog.getDate();
        if (dialog != null) {
            messageString = dialog.getBody();
        }

        int timeWidth = (int) Math.ceil(timePaint.measureText(timeString));
        timeLayout = new StaticLayout(timeString, timePaint, timeWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        timeLeft = getMeasuredWidth() - AndroidUtils.dp(15) - timeWidth;

        chatNameString = dialog.getTitle();

        int nameWidth = getMeasuredWidth() - chatNameLeft - AndroidUtils.dp(14) - timeWidth;

        nameWidth = Math.max(AndroidUtils.dp(12), nameWidth);
        CharSequence nameStringFinal = TextUtils.ellipsize(chatNameString.replace("\n", " "), currentNamePaint, nameWidth - AndroidUtils.dp(12), TextUtils.TruncateAt.END);
        try {
            chatNameLayout = new StaticLayout(nameStringFinal, currentNamePaint, nameWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e) {
            Log.e("tmessages", "", e);
        }

        int messageWidth = getMeasuredWidth() - AndroidUtils.dp(AndroidUtils.leftBaseline + 16);
        messageLeft = AndroidUtils.dp(AndroidUtils.leftBaseline);
        if (avatar != null) {
            avatar.setLeft(AndroidUtils.dp(16));
            avatar.setTop(AndroidUtils.dp(9));
        }

        if (checkMessage) {
            if (messageString == null) {
                messageString = "";
            }
            String mess = messageString.toString();
            if (mess.length() > 150) {
                mess = mess.substring(0, 150);
            }
            messageString = mess.replace("\n", " ");
        }
        messageWidth = Math.max(AndroidUtils.dp(12), messageWidth);
        CharSequence messageStringFinal = TextUtils.ellipsize(messageString, currentMessagePaint, messageWidth - AndroidUtils.dp(12), TextUtils.TruncateAt.END);
        try {
            messageLayout = new StaticLayout(messageStringFinal, currentMessagePaint, messageWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e) {
            DLogger.e("tmessages", e);
        }

        double widthpx;
        float left;
        if (chatNameLayout != null && chatNameLayout.getLineCount() > 0) {
            left = chatNameLayout.getLineRight(0);
            if (left == nameWidth) {
                widthpx = Math.ceil(chatNameLayout.getLineWidth(0));
                if (widthpx < nameWidth) {
                    chatNameLeft -= (nameWidth - widthpx);
                }
            }
        }
        if (messageLayout != null && messageLayout.getLineCount() > 0) {
            left = messageLayout.getLineRight(0);
            if (left == messageWidth) {
                widthpx = Math.ceil(messageLayout.getLineWidth(0));
                if (widthpx < messageWidth) {
                    messageLeft -= (messageWidth - widthpx);
                }
            }
        }
    }

    public void update() {
        if (getMeasuredWidth() != 0 || getMeasuredHeight() != 0) {
            buildLayout();
        } else {
            requestLayout();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (dialog == null) {
            return;
        }

        canvas.save();
        canvas.translate(chatNameLeft, chatNameTop);
        chatNameLayout.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(timeLeft, timeTop);
        timeLayout.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(messageLeft, messageTop);
        messageLayout.draw(canvas);
        canvas.restore();

        if (avatar != null) {
            avatar.draw(canvas);
        }
    }

    public void updateImage(Bitmap bitmap, int position) {
        avatar.onDownloadFinished(position, bitmap);
        invalidate();
    }

    public void updateAvatarsCount(int size) {
        avatar.setSize(size);
    }
}
