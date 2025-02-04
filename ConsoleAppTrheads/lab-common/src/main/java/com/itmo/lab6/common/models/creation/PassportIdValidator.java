package com.itmo.lab6.common.models.creation;

import java.util.Scanner;

import com.itmo.lab6.common.exceptions.WrongArgumentException;

public class PassportIdValidator {
    public static String validatePassportId(Scanner scanner) {
        String passportID;
        while (true) {
            System.out.print("enter passportID (length between 5 and 34): ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    if (line.length() >= 5 && line.length() <= 34) passportID = line;
                    else throw new WrongArgumentException();
                    break;
                } catch (WrongArgumentException exception) {

                }
            }
        }
        return passportID;
    }
}
