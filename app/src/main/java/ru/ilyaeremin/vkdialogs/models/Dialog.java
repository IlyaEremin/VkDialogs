package ru.ilyaeremin.vkdialogs.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import ru.ilyaeremin.vkdialogs.utils.Dates;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class Dialog implements Parcelable {
    String title;
    String body;
    int    date;
    @Nullable String[] userPics;
    String photo_100;
    String chat_active;
    int    chat_id;
    int    users_count;

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String[] getChatPhotoUrl() {
        if (hasPhoto()) return new String[]{photo_100};
        else return userPics;
    }

    public int[] getUserIds() {
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


    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeInt(this.date);
        dest.writeStringArray(this.userPics);
        dest.writeString(this.photo_100);
        dest.writeString(this.chat_active);
        dest.writeInt(this.chat_id);
        dest.writeInt(this.users_count);
    }

    protected Dialog(Parcel in) {
        this.title = in.readString();
        this.body = in.readString();
        this.date = in.readInt();
        this.userPics = in.createStringArray();
        this.photo_100 = in.readString();
        this.chat_active = in.readString();
        this.chat_id = in.readInt();
        this.users_count = in.readInt();
    }

    public static final Parcelable.Creator<Dialog> CREATOR = new Parcelable.Creator<Dialog>() {
        public Dialog createFromParcel(Parcel source) {
            return new Dialog(source);
        }

        public Dialog[] newArray(int size) {
            return new Dialog[size];
        }
    };
}
