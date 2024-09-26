package me.mrxeman;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.request.URLBuilder;
import com.github.fracpete.requests4j.response.JsonResponse;
import com.google.gson.JsonObject;

import java.net.CookieManager;
import java.net.URL;
import java.util.Map;

import static me.mrxeman.EduRequests.keyToken;


public class Global {

    public static String eduvulcan = "https://eduvulcan.pl";

    public static String email = PrivateInformation.email;
    public static String pass = PrivateInformation.pass;

    public static class EduAPI {

        private static String API = null;

        public static String main = "99f04f67-d7f6-4f30-9d02-61cdd656612f";
        public static String okresy = "18428deb-8f85-4f97-99b0-0ec35178edcc";
        public static String oceny = "83cbb389-f3ab-404d-8ecc-9e687b1b958c";


        public static void setAPI(String powiatToken) {
            if (API == null) {
                API = "https://uczen.eduvulcan.pl/" + powiatToken + "/api";
            }
        }

        public static String getAPI() {
            return API;
        }

        public static void mainRequest(CookieManager cookieManager) throws Exception {
            JsonResponse r = Requests.get(API + "/" + main)
                    .cookies(cookieManager)
                    .execute(new JsonResponse());
            System.out.println(r.json());
            JsonObject json = r.json().getAsJsonObject().get("uczniowie").getAsJsonArray().get(0).getAsJsonObject();
            EduParameters.idDziennik = json.get("idDziennik").getAsInt();
            EduParameters.imie = json.get("uczen").getAsString().split(" ")[0];
            EduParameters.nazwisko = json.get("uczen").getAsString().split(" ")[1];
            EduParameters.klasa = json.get("oddzial").getAsString();
            EduParameters.szkola = json.get("jednostka").getAsString();
        }

        public static void okresyRequest(CookieManager cookieManager, int okres) throws Exception {
            Map<String, String> parameters = Map.of(
                    "key", keyToken,
                    "idDziennik", EduParameters.idDziennik.toString()
            );
            URLBuilder builder = new URLBuilder(new URL(API + "/" + okresy));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(cookieManager)
                    .execute(new JsonResponse());
            System.out.println(r.json());
            EduParameters.selectedOkres = r.json().getAsJsonArray().get(okres % 2).getAsJsonObject().get("id").getAsString();
            System.out.println("Selected: " + EduParameters.selectedOkres);
        }

        public static void ocenyRequest(CookieManager cookieManager) throws Exception {
            Map<String, String> parameters = Map.of(
                    "key", keyToken,
                    "idOkresKlasyfikacyjny", EduParameters.selectedOkres
            );
            URLBuilder builder = new URLBuilder(new URL(API + "/" + oceny));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(cookieManager)
                    .execute(new JsonResponse());
            System.out.println(r.json());
        }

        public static void ocenyRequest(CookieManager cookieManager, String przedmiot) throws Exception {
            Map<String, String> parameters = Map.of(
                    "key", keyToken,
                    "idOkresKlasyfikacyjny", EduParameters.selectedOkres
            );
            URLBuilder builder = new URLBuilder(new URL(API + "/" + oceny));
            builder.append(parameters);
            JsonResponse r = Requests.get(builder.build())
                    .cookies(cookieManager)
                    .execute(new JsonResponse());
            System.out.println("Oceny: ");
            r.json().getAsJsonObject().get("ocenyPrzedmioty").getAsJsonArray().asList().forEach(element -> {
                if (element.getAsJsonObject().get("przedmiotNazwa").getAsString().equalsIgnoreCase(przedmiot)) {
                    element.getAsJsonObject().get("ocenyCzastkowe").getAsJsonArray().asList().forEach(ocena -> {
                        System.out.println(ocena.getAsJsonObject());
                    });
                }
            });
        }

    }

    public static class EduParameters {

        public static Integer idDziennik = null;
        public static String imie = null;
        public static String nazwisko = null;
        public static String klasa = null;
        public static String szkola = null;
        public static String selectedOkres = null;

    }

}
