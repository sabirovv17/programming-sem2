package com.itmo.lab6.client;

import java.io.Console;
import java.util.Scanner;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.RequestCode;

public class RegistrationManager {
    private final Scanner scanner;
    Console console = System.console();

    public RegistrationManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public Request registration() {
        System.out.print("do u have an account[y/n]?: ");
        while (true) {
            try {
                String line = scanner.nextLine().trim().toLowerCase();
                switch(line) {
                    case ("y"): 
                        return loginUser();
                    case ("n"): 
                        return registerUser();
                    default:
                        System.out.println("unknow option, please enter \"y\" or \"n\"");    
                }
            }catch (Exception exception) {}
        }
    }

    private Request loginUser() {
        System.out.println("---------\nLOGGING\n---------");
        String login; 
        String password; 
        while (true) {
            try {
                System.out.print("enter login: ");
                login = scanner.nextLine().trim();
                if (login.isEmpty()) throw new WrongArgumentException();
                break;
            } catch (WrongArgumentException exception) {}

        }
        while (true) {
            try {
                System.out.print("enter password: ");
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
                if (password.isEmpty()) throw new WrongArgumentException();
                break;
            }catch (WrongArgumentException exception) {}
        }

        return new Request.RequestBuilder()
            .setLogin(login)
            .setPassword(password)
            .setRequestCode(RequestCode.LOGIN)
            .build();        
    }
    private Request registerUser() {
        System.out.println("---------\nREGISTRATION\n---------");
        String login; 
        String password;
        while (true) {
            try {
                System.out.print("enter login: ");
                login = scanner.nextLine().trim();
                if (login.isEmpty()) throw new WrongArgumentException();
                break;
            } catch (WrongArgumentException exception) {}

        }
        while (true) {
            try {
                System.out.print("enter password: ");
                // password = scanner.nextLine().trim();
                char[] passwordChars = console.readPassword();
                password = new String(passwordChars);
                if (password.isEmpty()) throw new WrongArgumentException();
                break;
            }catch (WrongArgumentException exception) {}
        }

        return new Request.RequestBuilder()
            .setLogin(login)
            .setPassword(password)
            .setRequestCode(RequestCode.REGISTRATION)
            .build(); 
    }
    
}
