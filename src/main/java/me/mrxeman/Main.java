package me.mrxeman;

import static me.mrxeman.EduRequests.*;
import static me.mrxeman.Global.*;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting requests!!");
        startRequest();
        logInRequest();
        QueryRequest();
        mainLogInRequest();
        dziennikRequest();
        profileRequest();
        System.out.println("Done!");
    }

}