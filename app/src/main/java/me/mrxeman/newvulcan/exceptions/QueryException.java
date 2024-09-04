package me.mrxeman.newvulcan.exceptions;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import io.ktor.client.statement.HttpResponse;

public class QueryException extends RuntimeException {

    public QueryException(HttpResponse response, JSONObject json) {
        System.out.println("Status code: " + response.getStatus());
        System.out.println("Request time: " + response.getRequestTime());
        System.out.println("Response time: " + response.getResponseTime());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("URL: " + response.getCall().getRequest().getUrl());
        System.out.println("Query: " + json);
    }

    public QueryException() {
    }

    @Nullable
    @Override
    public String getMessage() {
        return "Couldn't get through the login process! Possibly invalid email?";
    }
}
