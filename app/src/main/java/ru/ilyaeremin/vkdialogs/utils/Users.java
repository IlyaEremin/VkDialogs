package ru.ilyaeremin.vkdialogs.utils;

import android.util.SparseArray;

import java.util.List;

import ru.ilyaeremin.vkdialogs.models.User;

/**
 * Created by Ilya Eremin on 24.01.2016.
 */
public class Users {

    private static volatile Users instance = null;

    public static Users getInstance(){
        Users localInstance = instance;
        if (localInstance == null) {
            synchronized (Dates.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Users();
                }
            }
        }
        return localInstance;
    }

    // TODO consider HashMap
    private final SparseArray<String> idToUserpic;

    private Users(){
        idToUserpic = new SparseArray<>();
    }

    public void populate(List<User> users){
        for (User user : users) {
            idToUserpic.put(user.getUid(), user.getPhoto());
        }
    }

    public String getPhotoForId(int id) {
        return idToUserpic.get(id);
    }

}
