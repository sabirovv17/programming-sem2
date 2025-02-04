package com.itmo.lab6.common.models.creation;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.itmo.lab6.common.models.Color;

public class ColorValidator {
    public static Color validateColor(Scanner scanner) {
        Color color;
        while (true) {
            System.out.print(String.format("enter color (%s): ", Color.getNames()));
            String line = scanner.nextLine().trim().toUpperCase();
            if (!line.isEmpty()) {
                try {
                    color = Color.valueOf(line);
                    break;
                } catch (NoSuchElementException exception) {

                } catch (NullPointerException exception) {

                } catch (IllegalStateException exception) {

                } catch (IllegalArgumentException exception) {

                }
            }
        }
        return color;
    }
    
}
