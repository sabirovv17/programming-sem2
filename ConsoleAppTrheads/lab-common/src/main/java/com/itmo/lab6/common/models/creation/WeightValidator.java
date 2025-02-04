package com.itmo.lab6.common.models.creation;

import java.util.Scanner;

import com.itmo.lab6.common.exceptions.WrongArgumentException;

public class WeightValidator {
    public static Long validateWeight(Scanner scanner) {
        Long weight;
        while(true) {
            System.out.print("enter <Long> weight >=0: ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    weight = Long.parseLong(line); 
                    if (weight < 0) throw new WrongArgumentException();
                    break;
                } catch (NumberFormatException exception) {

                } catch (WrongArgumentException exception) {

                }
            }
        }
        return weight;
    }
}
