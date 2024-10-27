package me.mrxeman.vulcan.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import static me.mrxeman.vulcan.utils.Global.*;

import android.annotation.SuppressLint;
import android.util.Log;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.form.FormData;
import com.github.fracpete.requests4j.request.URLBuilder;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class User {

    private int version = 1;
    private String email = null;
    private String password = null;
    private API api = null;

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

    public API getApi() {
        return api;
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
            api = new API();
        }

    }

    public void mainRequest() {
        new mainRequest().run();
    }

    public class API {

        private final String API;
        private final String WAPI;

        private final String main =         "Context";
        private final String okresy =       "OkresyKlasyfikacyjne";
        private final String oceny =        "Oceny";
        private final String frekwencja =   "Frekwencja";
        private final String lekcje =       "PlanZajec";
        private final String sprawdzian =   "SprawdzianyZadaniaDomowe";
        private final String wOdebrane =    "Odebrane";
        private final String wWyslane =    "Wyslane";

        private final String sprawSZ =      "SprawdzianSzczegoly";
        private final String wiadSZ =       "WiadomoscSzczegoly";

        public Integer idDziennik = null;
        public String imie = null;
        public String nazwisko = null;
        public String klasa = null;
        public String szkola = null;
        public String skrzynka = null;

        private JsonObject mainJson = null;

        public String firstSemester = null;
        public String secondSemester = null;
        private final ArrayList<String> semesters = new ArrayList<>();

        public MutableLiveData<ArrayList<JsonElement>> Oceny = new MutableLiveData<>(new ArrayList<>());
        public MutableLiveData<JsonElement> frekwencjaJson = new MutableLiveData<>(JsonNull.INSTANCE);
        public MutableLiveData<JsonElement> lekcjeJson = new MutableLiveData<>(JsonNull.INSTANCE);
        public MutableLiveData<JsonElement> testsJson = new MutableLiveData<>(JsonNull.INSTANCE);
        public MutableLiveData<JsonElement> RMessagesJson = new MutableLiveData<>(JsonNull.INSTANCE);
        public MutableLiveData<JsonElement> SMessagesJson = new MutableLiveData<>(JsonNull.INSTANCE);

        public API() {
            API = "https://uczen.eduvulcan.pl/" + powiatToken + "/api";
            WAPI = "https://wiadomosci.eduvulcan.pl/" + powiatToken + "/api";
            new Thread(this::loadRequest).start();
        }

        public void loadRequest() {
            try {
                mainRequest();
            } catch (Exception e) {
                Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - MAIN REQUEST");
                throw new RuntimeException(e);
            }
            try {
                okresyRequest();
            } catch (Exception e) {
                Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - OKRESY REQUEST");
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                try {
                    ocenyRequest();
                } catch (Exception e) {
                    Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - OCENY REQUEST");
                    throw new RuntimeException(e);
                }
            }).start();
            new Thread(() -> {
                try {
                    frekwencjaRequest();
                } catch (Exception e) {
                    Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - FREKWENCJA REQUEST");
                    throw new RuntimeException(e);
                }
            }).start();
            new Thread(() -> {
                try {
                    lekcjeRequest();
                } catch (Exception e) {
                    Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - LEKCJA REQUEST");
                    throw new RuntimeException(e);
                }
            }).start();
            new Thread(() -> {
                try {
                    sprawdzianRequest();
                } catch (Exception e) {
                    Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - SPRAWDZIAN REQUEST");
                    throw new RuntimeException(e);
                }
            }).start();
            new Thread(() -> {
                try {
                    wiadomosciRequest1();
                    wiadomosciRequest2();
                } catch (Exception e) {
                    Log.e(null, "SOMETHING WENT WRONG WITH LOADING! - WIADOMOSCI REQUEST");
                    throw new RuntimeException(e);
                }
            }).start();
        }

        private void mainRequest() throws Exception {
            JsonResponse r = Requests.get(API + "/" + main)
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            JsonObject json = r.json().getAsJsonObject().get("uczniowie").getAsJsonArray().get(0).getAsJsonObject();
            idDziennik = json.get("idDziennik").getAsInt();
            imie = json.get("uczen").getAsString().split(" ")[0];
            nazwisko = json.get("uczen").getAsString().split(" ")[1];
            klasa = json.get("oddzial").getAsString();
            szkola = json.get("jednostka").getAsString();
            skrzynka = json.get("globalKeySkrzynka").getAsString();
            mainJson = json;
            BasicResponse r1 = Requests.get("https://wiadomosci.eduvulcan.pl/" + powiatToken + "/LoginEndpoint.aspx")
                    .cookies(getCookieManager())
                    .allowRedirects(true)
                    .maxRedirects(99)
                    .connectTimeout(1000000)
                    .execute();
            Document doc = Jsoup.parse(r1.text());
            String wa = doc.select("input[name=wa]").attr("value");
            String wresult = doc.select("input[name=wresult]").attr("value");
            String wctx = doc.select("input[name=wctx]").attr("value");
            FormData parameters = new FormData();
            parameters.add("wa", wa);
            parameters.add("wresult", wresult);
            parameters.add("wctx", wctx);
            Requests.post("https://wiadomosci.eduvulcan.pl/" + powiatToken + "/LoginEndpoint.aspx")
                    .formData(parameters)
                    .cookies(getCookieManager())
                    .allowRedirects(true)
                    .connectTimeout(1000000)
                    .maxRedirects(99)
                    .execute();
        }

        private void okresyRequest() throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", keyToken);
            parameters.put("idDziennik", idDziennik.toString());
            URLBuilder builder = new URLBuilder(new URL(API + "/" + okresy));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            firstSemester = r.json().getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
            secondSemester = r.json().getAsJsonArray().get(1).getAsJsonObject().get("id").getAsString();
            semesters.add(firstSemester);
            semesters.add(secondSemester);
        }

        private void ocenyRequest() throws Exception {
            ArrayList<JsonElement> temp = new ArrayList<>();
            for (String ss : semesters) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("key", keyToken);
                parameters.put("idOkresKlasyfikacyjny", ss);
                URLBuilder builder = new URLBuilder(new URL(API + "/" + oceny));
                builder.append(parameters);
                JsonResponse r = Requests.get(builder.build())
                        .cookies(getCookieManager())
                        .connectTimeout(1000000)
                        .execute(new JsonResponse());
                temp.add(r.json());
            }
            Oceny.postValue(temp);
        }

        private void frekwencjaRequest() throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", keyToken);
            parameters.put("dataOd", mainJson.get("dziennikDataOd").getAsString().split("T")[0] + "T22:00:00.000Z");
            parameters.put("dataDo", mainJson.get("dziennikDataDo").getAsString().split("T")[0] + "T21:59:59.999Z");
            URLBuilder builder = new URLBuilder(new URL(API + "/" + frekwencja));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            frekwencjaJson.postValue(r.json());
        }

        private void lekcjeRequest() throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", keyToken);
            parameters.put("dataOd", mainJson.get("dziennikDataOd").getAsString());
            parameters.put("dataDo", mainJson.get("dziennikDataDo").getAsString());
            parameters.put("zakresDanych", "2");
            URLBuilder builder = new URLBuilder(new URL(API + "/" + lekcje));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            lekcjeJson.postValue(r.json());
        }

        private void sprawdzianRequest() throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", keyToken);
            parameters.put("dataOd", mainJson.get("dziennikDataOd").getAsString());
            parameters.put("dataDo", mainJson.get("dziennikDataDo").getAsString());
            URLBuilder builder = new URLBuilder(new URL(API + "/" + sprawdzian));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            testsJson.postValue(r.json());
        }

        private void wiadomosciRequest1() throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("globalKeySkrzynka", skrzynka);
            parameters.put("idLastWiadomosc", String.valueOf(0));
            parameters.put("pageSize", String.valueOf(9999));
            URLBuilder builder = new URLBuilder(new URL(WAPI + "/" + wOdebrane));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            RMessagesJson.postValue(r.json());
        }

        private void wiadomosciRequest2() throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("idLastWiadomosc", String.valueOf(0));
            parameters.put("pageSize", String.valueOf(9999));
            URLBuilder builder = new URLBuilder(new URL(WAPI + "/" + wWyslane));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse());
            SMessagesJson.postValue(r.json());
        }

        public JsonElement SprawdzianRequest(int id) throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("key", keyToken);
            parameters.put("id", String.valueOf(id));
            URLBuilder builder = new URLBuilder(new URL(API + "/" + sprawSZ));
            builder.append(parameters);
            return Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse()).json();
        }

        public JsonElement DetailMessageRequest(String sSkrzynka) throws Exception {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("apiGlobalKey", sSkrzynka);
            URLBuilder builder = new URLBuilder(new URL(WAPI + "/" + wiadSZ));
            builder.append(parameters);
            return Requests.get(builder.build())
                    .cookies(getCookieManager())
                    .connectTimeout(1000000)
                    .execute(new JsonResponse()).json();
        }
    }

}
