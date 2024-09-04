package me.mrxeman.newvulcan.exceptions;

import androidx.annotation.Nullable;

import io.ktor.client.statement.HttpResponse;
import io.ktor.http.Cookie;
import me.mrxeman.newvulcan.Extras.Global;

public class NoCookiesException extends RuntimeException {

    public NoCookiesException(HttpResponse response, String body) {
        System.out.println("Status code: " + response.getStatus());
        System.out.println("Request time: " + response.getRequestTime());
        System.out.println("Response time: " + response.getResponseTime());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("URL: " + response.getCall().getRequest().getUrl());
        System.out.println("Body: " + body);
        System.out.println("Current Cookies: " + Global.getCookies());
        System.out.println("Got Cookies: " + response.getHeaders().get("set-cookie"));
    }

    @Nullable
    @Override
    public String getMessage() {
        return "No cookies got! These cookies are needed and the site didn't give them!";
    }
}
