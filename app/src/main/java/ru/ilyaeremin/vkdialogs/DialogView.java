package ru.ilyaeremin.vkdialogs;

import android.content.Context;
import android.view.View;

/**
 * Created by Ilya Eremin on 1/22/16.
 */
public class DialogView extends View {
    
    public DialogView(Context context) {
        super(context);
    }

//    private static TextPaint namePaint;
//    private static TextPaint messagePaint;
//    private static TextPaint timePaint;
//
//    private static Paint backPaint;
//
//    private long    currentDialogId;
//    private int     lastMessageDate;
//    private boolean lastUnreadState;
//    private int     lastSendState;
//    private String  message;
//
//    private ImageReceiver  avatarImage;
//    private AvatarDrawable avatarDrawable;
//
//    private CharSequence lastPrintString = null;
//
//    private int          nameLeft;
//    private StaticLayout nameLayout;
//    private boolean      drawNameGroup;
//
//    private int timeLeft;
//    private int timeTop = AndroidUtils.dp(17);
//    private StaticLayout timeLayout;
//
//    private int checkDrawLeft;
//    private int checkDrawTop = AndroidUtils.dp(18);
//
//    private int messageTop = AndroidUtils.dp(40);
//    private int          messageLeft;
//    private StaticLayout messageLayout;
//
//    private int countTop = AndroidUtils.dp(39);
//    private int          countLeft;
//    private int          countWidth;
//    private StaticLayout countLayout;
//
//    private int avatarTop = AndroidUtils.dp(10);
//
//    private boolean isSelected;
//    private Dialog  dialog;
//
//    public DialogView(Context context) {
//        super(context);
//
//        if (namePaint == null) {
//            namePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
//            namePaint.setTextSize(AndroidUtils.dp(17));
//            namePaint.setColor(0xff212121);
//            namePaint.setTypeface(AndroidUtils.getTypeface("fonts/rmedium.ttf"));
//
//            messagePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
//            messagePaint.setTextSize(AndroidUtils.dp(16));
//            messagePaint.setColor(0xff8f8f8f);
//            messagePaint.linkColor = 0xff8f8f8f;
//
//
//            backPaint = new Paint();
//            backPaint.setColor(0x0f000000);
//
//            timePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
//            timePaint.setTextSize(AndroidUtils.dp(13));
//            timePaint.setColor(0xff999999);
//
//        }
//
//        setBackgroundResource(R.drawable.list_selector);
//
//        avatarImage = new ImageReceiver(this);
//        avatarImage.setRoundRadius(AndroidUtils.dp(26));
//        avatarDrawable = new AvatarDrawable();
//    }
//
//    public void setDialog(Dialog dialog) {
//        this.dialog = dialog;
//        update(0);
//    }
//
//    public long getDialogId() {
//        return currentDialogId;
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        avatarImage.onDetachedFromWindow();
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        avatarImage.onAttachedToWindow();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtils.dp(72));
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if (currentDialogId == 0) {
//            super.onLayout(changed, left, top, right, bottom);
//            return;
//        }
//        if (changed) {
//            buildLayout();
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//                getBackground().setHotspot(event.getX(), event.getY());
//            }
//        }
//        return super.onTouchEvent(event);
//    }
//
//    public void buildLayout() {
//        String nameString = "";
//        String timeString = "";
//        CharSequence messageString = "";
//        CharSequence printingString = null;
//        printingString = MessagesController.getInstance().printingStrings.get(currentDialogId);
//        TextPaint currentNamePaint = namePaint;
//        TextPaint currentMessagePaint = messagePaint;
//        boolean checkMessage = true;
//
//        drawNameGroup = true;
//        nameLeft = AndroidUtils.dp(14);
//
//        if (message == null) {
//            if (printingString != null) {
//                lastPrintString = messageString = printingString;
//                currentMessagePaint = messagePrintingPaint;
//            } else {
//                lastPrintString = null;
//            }
//            if (lastMessageDate != 0) {
//                timeString = Dates.stringForMessageListDate(lastMessageDate);
//            }
//        } else {
//            TLRPC.User fromUser = null;
//            TLRPC.Chat fromChat = null;
//            if (message.messageOwner.from_id > 0) {
//                fromUser = MessagesController.getInstance().getUser(message.messageOwner.from_id);
//            } else if (message.messageOwner.from_id < 0) {
//                fromChat = MessagesController.getInstance().getChat(-message.messageOwner.from_id);
//            }
//
//            if (lastMessageDate != 0) {
//                timeString = Dates.stringForMessageListDate(lastMessageDate);
//            } else {
//                timeString = LocaleController.stringForMessageListDate(message.messageOwner.date);
//            }
//            if (printingString != null) {
//                lastPrintString = messageString = printingString;
//                currentMessagePaint = messagePrintingPaint;
//            } else {
//                lastPrintString = null;
//                if (message.messageOwner instanceof TLRPC.TL_messageService) {
//                    messageString = message.messageText;
//                    currentMessagePaint = messagePrintingPaint;
//                } else {
//                    if (chat != null && chat.id > 0 && fromChat == null) {
//                        String name;
//                        if (message.isOutOwner()) {
//                            name = LocaleController.getString("FromYou", R.string.FromYou);
//                        } else if (fromUser != null) {
//                            name = UserObject.getFirstName(fromUser);
//                        } else if (fromChat != null) {
//                            name = fromChat.title;
//                        } else {
//                            name = "DELETED";
//                        }
//                        checkMessage = false;
//                        if (message.caption != null) {
//                            String mess = message.caption.toString();
//                            if (mess.length() > 150) {
//                                mess = mess.substring(0, 150);
//                            }
//                            mess = mess.replace("\n", " ");
//                            messageString = Emoji.replaceEmoji(AndroidUtils.replaceTags(String.format("<c#ff4d83b3>%s:</c> <c#ff808080>%s</c>", name, mess), AndroidUtils.FLAG_TAG_COLOR), messagePaint.getFontMetricsInt(), AndroidUtils.dp(20), false);
//                        } else {
//                            if (message.messageOwner.media != null && !message.isMediaEmpty()) {
//                                currentMessagePaint = messagePrintingPaint;
//                                messageString = Emoji.replaceEmoji(AndroidUtils.replaceTags(String.format("<c#ff4d83b3>%s:</c> <c#ff4d83b3>%s</c>", name, message.messageText), AndroidUtils.FLAG_TAG_COLOR), messagePaint.getFontMetricsInt(), AndroidUtils.dp(20), false);
//                            } else {
//                                if (message.messageOwner.message != null) {
//                                    String mess = message.messageOwner.message;
//                                    if (mess.length() > 150) {
//                                        mess = mess.substring(0, 150);
//                                    }
//                                    mess = mess.replace("\n", " ");
//                                    messageString = Emoji.replaceEmoji(AndroidUtils.replaceTags(String.format("<c#ff4d83b3>%s:</c> <c#ff808080>%s</c>", name, mess), AndroidUtils.FLAG_TAG_COLOR), messagePaint.getFontMetricsInt(), AndroidUtils.dp(20), false);
//                                }
//                            }
//                        }
//                    } else {
//                        if (message.caption != null) {
//                            messageString = message.caption;
//                        } else {
//                            messageString = message.messageText;
//                            if (message.messageOwner.media != null && !message.isMediaEmpty()) {
//                                currentMessagePaint = messagePrintingPaint;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        int timeWidth = (int) Math.ceil(timePaint.measureText(timeString));
//        timeLayout = new StaticLayout(timeString, timePaint, timeWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//        timeLeft = AndroidUtils.dp(15);
//
//        nameString = dialog.getTitle();
//
//        int nameWidth;
//
//        nameWidth = getMeasuredWidth() - nameLeft - AndroidUtils.dp(AndroidUtils.leftBaseline) - timeWidth;
//        nameLeft += timeWidth;
//
//        nameWidth = Math.max(AndroidUtils.dp(12), nameWidth);
//        CharSequence nameStringFinal = TextUtils.ellipsize(nameString.replace("\n", " "), currentNamePaint, nameWidth - AndroidUtils.dp(12), TextUtils.TruncateAt.END);
//        try {
//            nameLayout = new StaticLayout(nameStringFinal, currentNamePaint, nameWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//        } catch (Exception e) {
//            Log.e("tmessages", "", e);
//        }
//
//        int messageWidth = getMeasuredWidth() - AndroidUtils.dp(AndroidUtils.leftBaseline + 16);
//        int avatarLeft;
//        messageLeft = AndroidUtils.dp(16);
//        avatarLeft = getMeasuredWidth() - AndroidUtils.dp(61);
//        avatarImage.setImageCoords(avatarLeft, avatarTop, AndroidUtils.dp(52), AndroidUtils.dp(52));
//
//        if (checkMessage) {
//            if (messageString == null) {
//                messageString = "";
//            }
//            String mess = messageString.toString();
//            if (mess.length() > 150) {
//                mess = mess.substring(0, 150);
//            }
//            mess = mess.replace("\n", " ");
//            messageString = Emoji.replaceEmoji(mess, messagePaint.getFontMetricsInt(), AndroidUtils.dp(17), false);
//        }
//        messageWidth = Math.max(AndroidUtils.dp(12), messageWidth);
//        CharSequence messageStringFinal = TextUtils.ellipsize(messageString, currentMessagePaint, messageWidth - AndroidUtils.dp(12), TextUtils.TruncateAt.END);
//        try {
//            messageLayout = new StaticLayout(messageStringFinal, currentMessagePaint, messageWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//        } catch (Exception e) {
//            DLogger.e("tmessages", e);
//        }
//
//        double widthpx;
//        float left;
//        if (nameLayout != null && nameLayout.getLineCount() > 0) {
//            left = nameLayout.getLineRight(0);
//            if (left == nameWidth) {
//                widthpx = Math.ceil(nameLayout.getLineWidth(0));
//                if (widthpx < nameWidth) {
//                    nameLeft -= (nameWidth - widthpx);
//                }
//            }
//        }
//        if (messageLayout != null && messageLayout.getLineCount() > 0) {
//            left = messageLayout.getLineRight(0);
//            if (left == messageWidth) {
//                widthpx = Math.ceil(messageLayout.getLineWidth(0));
//                if (widthpx < messageWidth) {
//                    messageLeft -= (messageWidth - widthpx);
//                }
//            }
//        }
//    }
//
//    public void update(int mask) {
//        if (isDialogCell) {
//            TLRPC.Dialog dialog = MessagesController.getInstance().dialogs_dict.get(currentDialogId);
//            if (dialog != null && mask == 0) {
//                message = MessagesController.getInstance().dialogMessage.get(dialog.id);
//                lastUnreadState = message != null && message.isUnread();
//                unreadCount = dialog.unread_count;
//                lastMessageDate = dialog.last_message_date;
//                if (message != null) {
//                    lastSendState = message.messageOwner.send_state;
//                }
//            }
//        }
//
//        if (mask != 0) {
//            boolean continueUpdate = false;
//            if (isDialogCell) {
//                if ((mask & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
//                    CharSequence printString = MessagesController.getInstance().printingStrings.get(currentDialogId);
//                    if (lastPrintString != null && printString == null || lastPrintString == null && printString != null || lastPrintString != null && printString != null && !lastPrintString.equals(printString)) {
//                        continueUpdate = true;
//                    }
//                }
//            }
//            if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_AVATAR) != 0) {
//                if (chat == null) {
//                    continueUpdate = true;
//                }
//            }
//            if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_NAME) != 0) {
//                if (chat == null) {
//                    continueUpdate = true;
//                }
//            }
//            if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0) {
//                if (user == null) {
//                    continueUpdate = true;
//                }
//            }
//            if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_CHAT_NAME) != 0) {
//                if (user == null) {
//                    continueUpdate = true;
//                }
//            }
//            if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE) != 0) {
//                if (message != null && lastUnreadState != message.isUnread()) {
//                    lastUnreadState = message.isUnread();
//                    continueUpdate = true;
//                } else if (isDialogCell) {
//                    TLRPC.Dialog dialog = MessagesController.getInstance().dialogs_dict.get(currentDialogId);
//                    if (dialog != null && unreadCount != dialog.unread_count) {
//                        unreadCount = dialog.unread_count;
//                        continueUpdate = true;
//                    }
//                }
//            }
//            if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_SEND_STATE) != 0) {
//                if (message != null && lastSendState != message.messageOwner.send_state) {
//                    lastSendState = message.messageOwner.send_state;
//                    continueUpdate = true;
//                }
//            }
//
//            if (!continueUpdate) {
//                return;
//            }
//        }
//
//        TLRPC.FileLocation photo = null;
//        if (user != null) {
//            if (user.photo != null) {
//                photo = user.photo.photo_small;
//            }
//            avatarDrawable.setInfo(user);
//        } else if (chat != null) {
//            if (chat.photo != null) {
//                photo = chat.photo.photo_small;
//            }
//            avatarDrawable.setInfo(chat);
//        }
//        avatarImage.setImage(photo, "50_50", avatarDrawable, null, false);
//
//        if (getMeasuredWidth() != 0 || getMeasuredHeight() != 0) {
//            buildLayout();
//        } else {
//            requestLayout();
//        }
//
//        invalidate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (currentDialogId == 0) {
//            return;
//        }
//
//        if (isSelected) {
//            canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), backPaint);
//        }
//
//        if (nameLayout != null) {
//            canvas.save();
//            canvas.translate(nameLeft, AndroidUtils.dp(13));
//            nameLayout.draw(canvas);
//            canvas.restore();
//        }
//
//        canvas.save();
//        canvas.translate(timeLeft, timeTop);
//        timeLayout.draw(canvas);
//        canvas.restore();
//
//        if (messageLayout != null) {
//            canvas.save();
//            canvas.translate(messageLeft, messageTop);
//            messageLayout.draw(canvas);
//            canvas.restore();
//        }
//
//
//
//        avatarImage.draw(canvas);
//    }
//
//    protected void setDrawableBounds(Drawable drawable, int x, int y) {
//        setDrawableBounds(drawable, x, y, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//    }
//
//    protected void setDrawableBounds(Drawable drawable, int x, int y, int w, int h) {
//        if (drawable != null) {
//            drawable.setBounds(x, y, x + w, y + h);
//        }
//    }
//
}
