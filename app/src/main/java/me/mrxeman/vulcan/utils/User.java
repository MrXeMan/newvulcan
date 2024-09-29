package me.mrxeman.vulcan.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static me.mrxeman.vulcan.utils.Global.*;

import android.annotation.SuppressLint;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.form.FormData;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.JsonResponse;

import org.jetbrains.annotations.Contract;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class User {

    private int version = 1;
    private String email = null;
    private String password = null;

    private String requestVerificationToken = null;
    private String dziennikToken = null;
    private String profileToken = null;
    private String powiatToken = null;
    private String keyToken = null;

    public User(@NonNull String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            throw new RuntimeException("No email or password was given!");
        }
        this.email = email;
        this.password = password;
    }

    private User(@NonNull String email, String password, int version) {
        if (email.isEmpty() || password.isEmpty()) {
            throw new RuntimeException("No email or password was given!");
        }
        this.version = version;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getVersion() {
        return version;
    }

    private String getRequestVerificationToken() {
        return requestVerificationToken;
    }

    @NonNull
    @Override
    public String toString() {
        return version + ";" + email + ";" + password;
    }

    @NonNull
    public String toStringEncrypted() {
        return Base64.getEncoder().encodeToString(String.valueOf(version).getBytes()) + ";" + Base64.getEncoder().encodeToString(email.getBytes())
                + ";" + Base64.getEncoder().encodeToString(password.getBytes());
    }

    @SuppressLint("CommitPrefEdits")
    public void save() {
        System.out.println("Saved the user!");
        getPreferences().edit().putString("user", toStringEncrypted()).apply();
    }

    @NonNull
    @Contract(" -> new")
    public static User load() {
        if (getPreferences().contains("user")) {
            System.out.println("Shared preferences has user saved!");
            if (getPreferences().getString("user", null) == null) {
                throw new RuntimeException("No user found!");
            } else {
                System.out.println("Loading user...");
                String[] userLoaded = getPreferences().getString("user", null).split(";");
                ArrayList<String> userSettings = new ArrayList<>();
                for (String s : userLoaded) {
                    userSettings.add(
                            new String(Base64.getDecoder().decode(s))
                    );
                }
                int version = Integer.parseInt(userSettings.get(0));
                String email = userSettings.get(1);
                String password = userSettings.get(2);
                return new User(email, password, version);
            }
        } else {
            throw new RuntimeException("No user found!");
        }
    }

    private class PageLogIn {

        private void getStartingCookies() {
            request(
                    () -> {
                        Requests.get(vulcan)
                                .cookies(getCookieManager())
                                .execute();
                        return null;
                    }
            );
        }

        @Nullable
        private String getRequestVerification() {
            BasicResponse r = (BasicResponse) request(
                    () -> Requests.get(String.format("%s/logowanie", vulcan))
                            .cookies(getCookieManager())
                            .execute(new BasicResponse())
            );
            if (r == null) {
                return null;
            }
            try {
                Document doc = Jsoup.parse(r.text());
                return doc.select("input[name=__RequestVerificationToken]").attr("value");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        @Nullable
        private Boolean getCaptchaQuery() {
            JsonResponse r = (JsonResponse) request(
                    () -> Requests.post(String.format("%s/Account/QueryUserInfo", vulcan))
                            .cookies(getCookieManager())
                            .formData(new FormData()
                                    .add("Alias", email)
                                    .add("__RequestVerificationToken", requestVerificationToken)
                                    .add("Password", password)
                                    .add("captchaUser", ""))
                            .execute(new JsonResponse())
            );
            if (r == null) {
                return null;
            }
            try {
                return r.jsonObject().get("data").getAsJsonObject().get("ShowCaptcha").getAsBoolean();
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }

        public void run() {
            System.out.println(vulcan);
            getStartingCookies();
            System.out.println("Got starting cookies!");
            String temp = getRequestVerification();
            if (temp == null || temp.isEmpty()) {
                throw new RuntimeException("Couldn't get the request verification token!");
            } else {
                requestVerificationToken = temp;
                System.out.println("Got the request verification token!");
            }
            Boolean gotIt = getCaptchaQuery();
            if (gotIt == null) {
                throw new RuntimeException("Couldn't get the showCaptcha! Invalid everything.");
            }
            if (gotIt) {
                throw new RuntimeException("Please log in using the website. Captcha has blocked the account.");
            } else {
                System.out.println("Got through!");
            }
        }
    }

    public void PageLogIn() {
        new PageLogIn().run();
    }

    private class mainRequest {

        private void mainLogInRequest() {
            request(
                    () -> {
                        FormData formData = new FormData()
                                .add("Alias", email)
                                .add("__RequestVerificationToken", requestVerificationToken)
                                .add("Password", password)
                                .add("captchaUser", "");
                        Requests.post(String.format("%s/logowanie", vulcan))
                                .cookies(getCookieManager())
                                .formData(formData)
                                .allowRedirects(false)
                                .execute();
                        return null;
                    }
            );
        }

        @Nullable
        private String dziennikRequest() {
            BasicResponse r = (BasicResponse) request(
                    () -> Requests.get(vulcan)
                            .cookies(getCookieManager())
                            .execute()
            );
            try {
                assert r != null;
                Document doc = Jsoup.parse(r.text());
                return doc.select("a.connected-account").attr("href");
            } catch (UnsupportedEncodingException e) {
                System.out.println("ERROR: " + e);
                return null;
            }
        }

        @Nullable
        private Map<String, Object> profileRequest() {
            final String[] location = {null};
            HashMap<String, Object> map = new HashMap<>();
            BasicResponse r = (BasicResponse) request(
                    () -> Requests.get(String.format("%s%s", vulcan, dziennikToken))
                            .cookies(getCookieManager())
                            .allowRedirects(true)
                            .maxRedirects(99)
                            .addExecutionListener(requestExecutionEvent -> {
                                if (location[0] == null) {
                                    location[0] = String.valueOf(requestExecutionEvent.getReponse().headers().get("location"));
                                }
                            })
                            .execute()
            );
            if (r == null) {
                return null;
            }
            try {
                Document doc = Jsoup.parse(r.text());
                String url = doc.select("form[method=POST]").attr("action");
                String wresult = doc.select("input[name=wresult]").attr("value");
                map.put("powiatToken", location[0].split(".pl/")[1].split("/")[0]);
                map.put("profileToken", location[0].split("=")[1].split("]")[0]);
                map.put("formData", new FormData()
                        .add("wa", "wsignin1.0")
                        .add("wresult", wresult)
                        .add("wctx", "nslo=1")
                );
                map.put("url", url);
                return map;
            } catch (UnsupportedEncodingException e) {
                System.out.println("ERROR: " + e);
                return null;
            }
        }

        @Nullable
        private Map<String, Object> betweenRequest(String url, FormData formData) {
            HashMap<String, Object> map = new HashMap<>();
            BasicResponse r = (BasicResponse) request(
                    () -> Requests.post(url)
                            .cookies(getCookieManager())
                            .formData(formData)
                            .execute()
            );
            if (r == null) {
                return null;
            }
            try {
                Document doc = Jsoup.parse(r.text());
                String wresult = doc.select("input[name=wresult]").attr("value");
                String url2 = doc.select("form[method=POST]").attr("action");
                FormData formData2 = new FormData()
                        .add("wa", "wsignin1.0")
                        .add("wresult", wresult)
                        .add("wctx", "auth=studentEV&nslo=1");
                map.put("url", url2);
                map.put("formData", formData2);
                return map;
            } catch (UnsupportedEncodingException e) {
                System.out.println("ERROR: " + e);
                return null;
            }
        }

        @Nullable
        private String keyRequest(String url, FormData formData) {
            BasicResponse r = (BasicResponse) request(
                    () -> Requests.post(url)
                            .cookies(getCookieManager())
                            .formData(formData)
                            .allowRedirects(true)
                            .maxRedirects(99)
                            .execute()
            );
            if (r == null) {
                return null;
            }
            return r.rawResponse().request().url().toString().split("App/")[1].split("/")[0];
        }

        public void run() {
            mainLogInRequest();
            String temp = dziennikRequest();
            if (temp == null || temp.isEmpty()) {
                throw new RuntimeException("Couldn't get the dziennik token!");
            }
            dziennikToken = temp;
            Map<String, Object> profileParameters = profileRequest();
            if (profileParameters == null || profileParameters.containsValue(null)) {
                throw new RuntimeException("Profile requests has been unsuccessful!");
            }
            powiatToken = (String) profileParameters.get("powiatToken");
            profileToken = (String) profileParameters.get("profileToken");
            Map<String, Object> middleParams = betweenRequest((String) profileParameters.get("url"), (FormData) profileParameters.get("formData"));
            if (middleParams == null || middleParams.containsValue(null)) {
                throw new RuntimeException("Preperations for key request has been unsuccessful!");
            }
            temp = keyRequest((String) middleParams.get("url"), (FormData) middleParams.get("formData"));
            if (temp == null || temp.isEmpty()) {
                throw new RuntimeException("Key request has been unsuccessful!");
            }
            keyToken = temp;
        }

    }

    public void mainRequest() {
        new mainRequest().run();
    }

}
