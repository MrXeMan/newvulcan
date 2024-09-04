package me.mrxeman.newvulcan.exceptions;

import androidx.annotation.Nullable;

public class LoggedNoFoundUserStringException extends RuntimeException {


    @Nullable
    @Override
    public String getMessage() {
        return "Shared preferences show there was a logged user but there is no user string!";
    }
}
