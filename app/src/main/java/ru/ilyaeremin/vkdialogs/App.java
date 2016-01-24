package ru.ilyaeremin.vkdialogs;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.vk.sdk.VKSdk;

/**
 * Created by Ilya Eremin on 17.01.2016.
 */
public class App extends Application {

    public static          Context applicationContext;
    public static volatile Handler applicationHandler;

    @Override public void onCreate() {
        super.onCreate();
        applicationContext = this;
        VKSdk.initialize(this);
        applicationHandler = new Handler(applicationContext.getMainLooper());
    }

}
