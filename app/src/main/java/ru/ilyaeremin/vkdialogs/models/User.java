package ru.ilyaeremin.vkdialogs.models;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class User {
    int uid;
    String photo_50;

    public User(int id, String photo_50) {
        this.uid = id;
        this.photo_50 = photo_50;
    }

    public int getUid() {
        return uid;
    }

    public String getPhoto_50() {
        return photo_50;
    }
}
