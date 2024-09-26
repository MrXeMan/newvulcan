package me.mrxeman;

import java.util.Arrays;
import java.util.Scanner;

import static me.mrxeman.EduRequests.*;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting requests!!");
        startRequest();
        logInRequest();
        QueryRequest();
        mainLogInRequest();
        dziennikRequest();
        profileRequest();
        System.out.println("Done requests! Doing customs...");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Command: ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("quit")) break;
            System.out.println("Arguments: ");
            String[] arguments = scanner.nextLine().strip().split(";");
            System.out.println(Arrays.toString(arguments));
            sendRequest(response, arguments);
        }

        System.out.println("Done!");
    }

}