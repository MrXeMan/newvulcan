package me.mrxeman;


import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.event.RequestExecutionEvent;
import com.github.fracpete.requests4j.event.RequestExecutionListener;
import com.github.fracpete.requests4j.form.FormData;
import com.github.fracpete.requests4j.request.URLBuilder;
import com.github.fracpete.requests4j.response.BasicResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.beans.Encoder;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import static me.mrxeman.Global.*;


public class EduRequests {

    public static CookieManager cookieManager = new CookieManager();
    public static final String user_agent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:65.0) Gecko/20100101 Firefox/65.0";

    public static String requestToken = null;
    public static String dziennikToken = null;
    public static String powiatToken = null;
    public static String profileToken = null;
    public static String keyToken = null;
    public static String jquery = "";

    public static void startRequest() throws Exception {
        Requests.get(eduvulcan)
                        .cookies(cookieManager)
                        .execute();
        System.out.println(cookieManager.getCookieStore().getCookies());
    }

    public static void logInRequest() throws Exception {
        BasicResponse r = Requests.get("%s/logowanie".formatted(eduvulcan))
                .cookies(cookieManager)
                .execute();
        Document doc = Jsoup.parse(r.text());
        System.out.println(cookieManager.getCookieStore().getCookies());
        requestToken = doc.select("input[name=__RequestVerificationToken]").attr("value");
        System.out.println(requestToken);
        jquery = doc.select("script").first().attr("src");
    }

    public static void QueryRequest() throws Exception {
        FormData formData = new FormData()
                .add("alias", email)
                .add("__RequestVerificationToken", requestToken);
        BasicResponse r = Requests.post("https://eduvulcan.pl/Account/QueryUserInfo")
                .formData(formData)
                .cookies(cookieManager)
                .execute();
        System.out.println(r.text());
    }

    public static void mainLogInRequest() {
        FormData formData = new FormData()
                .add("Alias", email)
                .add("__RequestVerificationToken", requestToken)
                .add("Password", pass)
                .add("captchaUser", "");
        try {
            Requests.post("%s/logowanie".formatted(eduvulcan))
                    .cookies(cookieManager)
                    .formData(formData)
                    .allowRedirects(false)
                    .execute();
        } catch (Exception ignored) {
        }
        System.out.println(cookieManager.getCookieStore().getCookies());
        System.out.println();
    }

    public static void dziennikRequest() throws Exception {
        BasicResponse r = Requests.get("%s".formatted(eduvulcan))
                .cookies(cookieManager)
                .execute();
        Document doc = Jsoup.parse(r.text());
        dziennikToken = doc.select("a.connected-account").attr("href");
        System.out.println(dziennikToken);
    }

    public static void profileRequest() throws Exception {
        ArrayList<String> redirects = new ArrayList<>();
        BasicResponse r1 = Requests.get("%s%s".formatted(eduvulcan, dziennikToken))
                .cookies(cookieManager)
                .allowRedirects(true)
                .maxRedirects(99)
                .addExecutionListener(new RequestExecutionListener() {
                    @Override
                    public void requestExecuted(RequestExecutionEvent requestExecutionEvent) {
                        redirects.add(String.valueOf(requestExecutionEvent.getReponse().headers().get("location")));
                    }
                })
                .execute();
        redirects.removeLast();
        Document doc = Jsoup.parse(r1.text());
        String url = doc.select("form[method=POST]").attr("action");
        String wresult = doc.select("input[name=wresult]").attr("value");
        powiatToken = redirects.get(0).split(".pl/")[1].split("/")[0];
        profileToken = redirects.get(0).split("=")[1].split("]")[0];
        FormData formData = new FormData()
                .add("wa", "wsignin1.0")
                .add("wresult", wresult)
                .add("wctx", "nslo=1");
        BasicResponse r2 = Requests.post(url)
                .cookies(cookieManager)
                .formData(formData)
                .execute();
        Document doc2 = Jsoup.parse(r2.text());
        String wresult2 = doc2.select("input[name=wresult]").attr("value");
        String url2 = doc2.select("form[method=POST]").attr("action");
        FormData formData2 = new FormData()
                .add("wa", "wsignin1.0")
                .add("wresult", wresult2)
                .add("wctx", "auth=studentEV&nslo=1");
        BasicResponse r3 = Requests.post(url2)
                .cookies(cookieManager)
                .formData(formData2)
                .allowRedirects(true)
                .maxRedirects(99)
                .execute();
        keyToken = r3.rawResponse().request().url().toString().split("App/")[1].split("/")[0];
    }

    public static String sendRequest() {



        return "";
    }

}
