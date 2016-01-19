package ru.ilyaeremin.vkdialogs.models;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class Dialog {
    String title;
    String lastMessage;
    String timeOfLastImage;
    long[] userIds;
    String photoUrl;

    public Dialog(String title, String lastMessage, String timeOfLastImage, long[] userIds) {
        this.title = title;
        this.lastMessage = lastMessage;
        this.timeOfLastImage = timeOfLastImage;
        this.userIds = userIds;
    }

    public long[] getUserIds() {
        return userIds;
    }

    public String getTitle() {
        return title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimeOfLastImage() {
        return timeOfLastImage;
    }
}
