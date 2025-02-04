package com.itmo.lab6.common.models.creation;

import java.util.Scanner;

import com.itmo.lab6.common.models.Location;

public class LocationValidator {
    public static Location validateLocation(Scanner scanner) {
        float x;
        while (true) {
            System.out.print("enter <float> x: ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    x = Float.parseFloat(line);
                    break;
                } catch (NumberFormatException exception) {

                }
            }
        }
        Double y;
        while (true) {
            System.out.print("enter <Double> y: ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    y = Double.parseDouble(line);
                    break;
                } catch (NumberFormatException exception) {

                }
            }
        }
        Long z;
        while (true) {
            System.out.print("enter <Long> z: "); 
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    z = Long.parseLong(line);
                    break;
                } catch (NumberFormatException exception) {

                }
            }
        }
        return new Location(x, y, z);
    }
}
