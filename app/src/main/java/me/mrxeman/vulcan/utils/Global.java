package me.mrxeman.vulcan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.github.fracpete.requests4j.response.Response;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class Global {

     private static final CookieManager cookieManager = new CookieManager();
     private static SharedPreferences preferences = null;

     public static User user = null;
     public static String email = null;
     public static String password = null;

     public static final URL vulcan;
     private static boolean running = false;

    static {
        try {
            vulcan = new URL("https://eduvulcan.pl");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPreferences(SharedPreferences pref) {
        if (preferences == null) {
            preferences = pref;
        }
    }


    public static CookieManager getCookieManager() {
         return cookieManager;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    @Nullable
    public static Response request(Callable<Response> fun) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Response> result = es.submit(fun);
        try {
            Response r = result.get();
            es.shutdown();
            return r;
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("ERROR: " + e);
            es.shutdown();
            return null;
        }
    }

    public static void run(Callable<Void> fun) {
        if (running) {
            System.out.println("Already running!");
            return;
        }
        running = true;
        new Thread(
                () -> {
                    try {
                        fun.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    running = false;
                }
        ).start();
    }

    public static void run(@NotNull Function0<Unit> function) {
        if (running) {
            System.out.println("Already running!");
            return;
        }
        running = true;
        new Thread(
                () -> {
                    try {
                        function.invoke();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    running = false;
                }
        ).start();
    }

    @NonNull
    public static Integer getDefaultColor(@NonNull String key) {
        switch(key) {
            case "importance1color": {
                return Color.rgb(0, 255, 0);
            }
            case "importance2color": {
                return Color.rgb(255, 200, 48);
            }
            case "importance3color": {
                return Color.rgb(255, 0, 0);
            }
            default: {
                return Color.rgb(0, 0, 0);
            }
        }
    }

    @NonNull
    public static ArrayList<String> getImportanceKeys() {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.add("importance1color");
        toReturn.add("importance2color");
        toReturn.add("importance3color");
        return toReturn;
    }
}
