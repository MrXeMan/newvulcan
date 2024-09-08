package me.mrxeman.newvulcan.Extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.chaquo.python.PyObject;
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
import io.ktor.util.Hash;

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

    public static void addPyCookies(@NonNull Map<PyObject, PyObject> newCookies) throws Exception {
        if (newCookies.values().stream().anyMatch(Objects::isNull)) {
            throw new Exception("Some cookies had null value inside! Invalid!");
        }
        HashMap<String, String> toReturn = new HashMap<>();
        for (PyObject pyObject : newCookies.keySet()) {
            PyObject pyObject1 = newCookies.get(pyObject);
            assert pyObject1 != null;
            toReturn.putIfAbsent(pyObject.toString(), pyObject1.toString());
        }
        addCookies(toReturn);
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

    @NonNull
    public static HashMap<String, String> getCookiesList() {
        HashMap<String, String> toReturn = new HashMap<>();
        for (Cookie cookie : cookies) {
            toReturn.putIfAbsent(cookie.getName(), cookie.getValue());
        }
        return toReturn;
    }

    public static void clearCookies() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookies", null);
        editor.apply();
        cookies.clear();
    }

}
