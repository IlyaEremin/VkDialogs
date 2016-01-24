package ru.ilyaeremin.vkdialogs.models;

import android.text.TextUtils;

import ru.ilyaeremin.vkdialogs.utils.Dates;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class Dialog {
    String   title;
    String   body;
    int      date;
    String[] userPics;
    String   photo_100;
    String   chat_active;
    int      chat_id;
    int      users_count;

    public String[] getUserPics() {
        return userPics;
    }

    public Dialog(String title, String body, int timeOfLastImage, long[] userIds) {
        this.title = title;
        this.body = body;
        this.date = timeOfLastImage;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String[] getChatPhotoUrl() {
        if(hasPhoto()) return new String[]{photo_100};
        else return userPics;
    }

    public int[] getUserIds(){
        String[] users = chat_active.replaceAll(" ", "").split(",");
        int[] userIds = new int[users.length];
        for (int i = 0; i < users.length; i++) {
            userIds[i] = Integer.valueOf(users[i]);
        }
        return userIds;
    }

    public String getDate() {
        return Dates.getInstance().stringForMessageListDate(date);
    }

    public boolean isChat() {
        return chat_id != 0;
    }

    public boolean hasPhoto() {
        return !TextUtils.isEmpty(photo_100);
    }

    public boolean isDeleted() {
        return users_count == 0;
    }
}
