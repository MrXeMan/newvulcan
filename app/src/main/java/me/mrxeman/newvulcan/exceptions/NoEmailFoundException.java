package me.mrxeman.newvulcan.exceptions;

public class NoEmailFoundException extends RuntimeException {

    public NoEmailFoundException() {
        System.out.println("No email found! Email is needed to process through the login process.");
    }

}
