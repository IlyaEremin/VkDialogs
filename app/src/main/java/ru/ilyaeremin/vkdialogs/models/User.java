package ru.ilyaeremin.vkdialogs.models;

/**
 * Created by Ilya Eremin on 18.01.2016.
 */
public class User {
    long id;
    String photoUrl;

    public User(long id, String photoUrl) {
        this.id = id;
        this.photoUrl = photoUrl;
    }

    public long getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
