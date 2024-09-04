package me.mrxeman.newvulcan.exceptions;

import androidx.annotation.Nullable;

import org.jsoup.nodes.Document;

public class VerificationException extends RuntimeException{

    public VerificationException(Document doc) {
        System.out.println(doc);
    }
    @Nullable
    @Override
    public String getMessage() {
        return "There was no element with the verification token!";
    }
}
