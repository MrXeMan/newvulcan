package me.mrxeman.newvulcan.exceptions;

import androidx.annotation.Nullable;

public class InvalidUserStringVersionException extends RuntimeException {

    public InvalidUserStringVersionException(int version) {
        System.out.println("Invalid user string version found! Version found: " + version);
    }


}
