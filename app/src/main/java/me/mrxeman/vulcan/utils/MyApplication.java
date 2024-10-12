package me.mrxeman.vulcan.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

        Global.setPreferences(MyApplication.context.getSharedPreferences("APP", Context.MODE_PRIVATE));
    }

    public static Context getContext() {
        return context;
    }

}
