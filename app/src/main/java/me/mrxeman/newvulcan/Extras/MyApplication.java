package me.mrxeman.newvulcan.Extras;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }

    public static Context getContext() {
        return context;
    }

}
