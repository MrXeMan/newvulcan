package me.mrxeman.newvulcan.Extras;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.ktor.http.Cookie;
import io.ktor.http.CookieEncoding;

public class Global {

    private static final Boolean DEBUG = true;

    private static final SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("APP", Context.MODE_PRIVATE);

    public static String stoppingCode = "////";
    private static ArrayList<Cookie> cookies = new ArrayList<>();

    public static void saveCookies() {
        if (DEBUG) return;
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String cookiesString = gson.toJson(cookies);
        editor.putString("cookies", cookiesString);
        editor.apply();
    }

    public static void loadCookies() {
        if (DEBUG) return;
        String cookiesString = sharedPreferences.getString("cookies", null);
        if (cookiesString == null) return;
        Gson gson = new Gson();
        TypeToken<ArrayList<Cookie>> type = new TypeToken<ArrayList<Cookie>>() {};
        cookies = gson.fromJson(cookiesString, type);
    }

    public static void addCookies(String name, String value) throws Exception {
        if (name == null || value == null) throw new Exception("Some cookies had null value inside! Invalid!");
        addCookies(new Cookie(name, value, CookieEncoding.RAW, 0, null, null, null, false, false, Collections.emptyMap()));
    }

    public static void addCookies(Cookie cookie) {
        if (cookies.contains(cookie)) return;
        cookies.add(cookie);
    }

    public static void addCookies(@NonNull Map<String, String> newCookies) throws Exception {
        if (newCookies.values().stream().anyMatch(Objects::isNull)) {
            throw new Exception("Some cookies had null value inside! Invalid!");
        }
        for (String name : newCookies.keySet()) {
            String value = newCookies.get(name);
            addCookies(name, value);
        }
    }

    public static void addCookies(List<Cookie> cookieList) {
        for (Cookie cookie : cookieList) {
            addCookies(cookie);
        }
    }

    public static List<Cookie> getCookies() {
        return cookies;
    }

    @NonNull
    public static String getCookiesString() {
        StringBuilder toReturn = new StringBuilder();
        for (Cookie cookie : cookies) {
            toReturn.append(";").append(cookie.getName()).append("=").append(cookie.getValue());
        }
        toReturn.deleteCharAt(0);
        return toReturn.toString();
    }

    public static void clearCookies() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookies", null);
        editor.apply();
        cookies.clear();
    }

}
