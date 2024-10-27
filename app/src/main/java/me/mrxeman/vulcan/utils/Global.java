package me.mrxeman.vulcan.utils;

import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.github.fracpete.requests4j.response.Response;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class Global {

    private static final CookieManager cookieManager = new CookieManager();
    private static SharedPreferences preferences = null;
    private static SharedPreferences defPref = null;

    public static User user = null;
    public static String email = null;
    public static String password = null;

    public static final URL vulcan;
    private static boolean running = false;

    public static final DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static final int reloadCooldown = 300_000;
    public static boolean reloadRequest = false;

    static {
       try {
           vulcan = new URL("https://eduvulcan.pl");
       } catch (MalformedURLException e) {
           throw new RuntimeException(e);
       }
       Importance.load();
       Attendance.load();
    }

    public static void setPreferences(SharedPreferences pref) {
        if (preferences == null) {
            preferences = pref;
            defPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
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

    public static LocalDate convertToLocalDateViaMilisecond(@NonNull Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static SharedPreferences getDefaultPreferences() {
        return defPref;
    }

    public static class Importance {

        static final HashMap<String, Integer> imp = new HashMap<>();

        public static void load() {
            imp.put("importance1color", Color.rgb(0 ,255, 0));
            imp.put("importance2color", Color.rgb(255, 200, 48));
            imp.put("importance3color", Color.rgb(255, 0, 0));
        }

        @NonNull
        public static Integer getDefaultColor(@NonNull String key) {
            if (imp.containsKey(key)) {
                return Objects.requireNonNull(imp.get(key));
            } else {
                return Color.rgb(0, 0, 0);
            }
        }

        @NonNull
        public static ArrayList<String> getKeys() {
            return new ArrayList<>(imp.keySet());
        }

    }

    public static class Attendance {

        static final HashMap<String, Integer> att = new HashMap<>();
        static final HashMap<String, String> att_text = new HashMap<>();

        public static void load() {
            att.put("attend_color", Color.rgb(0, 255, 0));
            att.put("absent_color", Color.rgb(120, 0, 0));
            att.put("absent_ex_color", Color.rgb(0, 255, 247));
            att.put("absent_sch_color", Color.rgb(255, 0, 0));
            att.put("tardiness_color", Color.rgb(252, 151, 151));
            att.put("tardiness_ex_color", Color.rgb(0, 42, 255));
            att.put("leave_color", Color.rgb(120, 3, 255));

            att_text.put("attend_text", "Obecnosc");
            att_text.put("absent_text", "Nieobecnosc");
            att_text.put("absent_ex_text", "Nieobecnosc usprawiedliwiona");
            att_text.put("absent_sch_text", "Nieobecnosc z przyczyn szkolnych");
            att_text.put("tardiness_text", "Spoznienie");
            att_text.put("tardiness_ex_text", "Spoznienie usprawiedliwione");
            att_text.put("leave_text", "Zwolnienie");
        }

        @Nullable
        @Contract(pure = true)
        public static String convert(int key) {
            switch (key) {
                case 1: {
                    return "attend_color";
                }
                case 2: {
                    return "absent_color";
                }
                case 3: {
                    return "absent_ex_color";
                }
                case 4: {
                    return "tardiness_color";
                }
                case 5: {
                    return "tardiness_ex_color";
                }
                case 6: {
                    return "absent_sch_color";
                }
                case 7: {
                    return "leave_color";
                }
            }
            return null;
        }

        @Nullable
        @Contract(pure = true)
        public static String convertText(int key) {
            switch (key) {
                case 1: {
                    return "attend_text";
                }
                case 2: {
                    return "absent_text";
                }
                case 3: {
                    return "absent_ex_text";
                }
                case 4: {
                    return "tardiness_text";
                }
                case 5: {
                    return "tardiness_ex_text";
                }
                case 6: {
                    return "absent_sch_text";
                }
                case 7: {
                    return "leave_text";
                }
            }
            return null;
        }

        @NonNull
        public static ArrayList<String> getKeys() {
            return new ArrayList<>(att.keySet());
        }

        @NonNull
        public static Integer getDefaultColor(@NonNull String key) {
            if (att.containsKey(key)) {
                return Objects.requireNonNull(att.get(key));
            } else {
                return Color.rgb(0, 0, 0);
            }
        }

        @NonNull
        public static String getDefaultText(@NonNull String key) {
            if (att_text.containsKey(key)) {
                return Objects.requireNonNull(att_text.get(key));
            } else {
                return "";
            }
        }

        @NonNull
        public static Integer getDefaultColor(int keyInt) {
            String key = convert(keyInt);
            if (key == null) throw new RuntimeException("THERE IS SOMETHING WRONG WITH CONVERT: " +  keyInt);
            return getDefaultColor(key);
        }

        @NonNull
        public static String getDefaultText(int keyInt) {
            String key = convertText(keyInt);
            if (key == null) throw new RuntimeException("THERE IS SOMETHING WRONG WITH CONVERT: " +  keyInt);
            return getDefaultText(key);
        }

    }
}
