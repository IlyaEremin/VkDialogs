package ru.ilyaeremin.vkdialogs.models;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class User {
    int uid;
    String photo_100;

    public User(int id, String photo_100) {
        this.uid = id;
        this.photo_100 = photo_100;
    }

    public int getUid() {
        return uid;
    }

    public String getPhoto() {
        return photo_100;
    }
}
