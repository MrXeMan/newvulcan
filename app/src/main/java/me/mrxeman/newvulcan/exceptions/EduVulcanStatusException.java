package me.mrxeman.newvulcan.exceptions;

import androidx.annotation.Nullable;

import io.ktor.http.HttpStatusCode;

public class EduVulcanStatusException extends RuntimeException {

    public EduVulcanStatusException(HttpStatusCode statusCode) {
        System.out.println(statusCode);
    }

    @Nullable
    @Override
    public String getMessage() {
        return "Couldn't get to EduVulcan's site! Possibly blocked.";
    }
}
