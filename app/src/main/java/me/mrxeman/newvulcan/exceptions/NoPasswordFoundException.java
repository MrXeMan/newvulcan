package me.mrxeman.newvulcan.exceptions;

public class NoPasswordFoundException extends RuntimeException {

    public NoPasswordFoundException() {
        System.out.println("No password found! Password is needed!");
    }

}
