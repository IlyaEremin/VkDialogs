package ru.ilyaeremin.vkdialogs;

import android.app.Application;
import android.content.Context;

import com.vk.sdk.VKSdk;

/**
 * Created by Ilya Eremin on 17.01.2016.
 */
public class App extends Application {

    public static Context applicationContext;

    @Override public void onCreate() {
        super.onCreate();
        applicationContext = this;
        VKSdk.initialize(this);
    }

}
