package com.itmo.lab6.common.models.creation;

import java.util.Scanner;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.models.Coordinates;

public class CoordinatesValidator {
    public static Coordinates validateCoordinates(Scanner scanner) {
        Double x;
        while (true) {
            System.out.print("enter <Double> x coordinate >=-442: ");
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                try {
                    x = Double.parseDouble(line);
                    if (x < -442) throw new WrongArgumentException();
                    break;
                } catch(NumberFormatException exception) {

                } catch (WrongArgumentException exception) {}
            }
        }
        Integer y; 
        while (true) {
            System.out.print("enter <Integer> y coordinate >=-258: "); 
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                try {
                    y = Integer.parseInt(line);
                    if (x < -258) throw new WrongArgumentException();
                    break;
                } catch (NumberFormatException exception) {

                } catch (WrongArgumentException exception) {

                }
            }
        }
        return new Coordinates(x, y);
    }
}
