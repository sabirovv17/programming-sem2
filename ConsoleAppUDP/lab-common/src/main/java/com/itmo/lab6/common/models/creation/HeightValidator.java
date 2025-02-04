package com.itmo.lab6.common.models.creation;

import java.util.Scanner;

import com.itmo.lab6.common.exceptions.WrongArgumentException;

public class HeightValidator {
    public static long validateHeight(Scanner scanner) {
        long height;
        while (true) {
            System.out.print("enter <long> height >=0: ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    height = Long.parseLong(line);
                    if (height < 0) throw new WrongArgumentException();
                    break;
                } catch (NumberFormatException exception) {

                } catch (WrongArgumentException exception) {

                }
            }
        }
        return height;
    }
}
