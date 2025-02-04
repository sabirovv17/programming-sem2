package com.itmo.lab6.common.models.creation;

import java.util.Scanner;

import com.itmo.lab6.common.models.Coordinates;
import com.itmo.lab6.common.models.Location;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.models.Color;


public class NameValidator {
    public static Person validateName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("enter person name: ");
            name = scanner.nextLine().trim();
            if (!name.isEmpty()) break;
        }
        Coordinates coordinates = CoordinatesValidator.validateCoordinates(scanner);
        long height = HeightValidator.validateHeight(scanner);
        Long weight = WeightValidator.validateWeight(scanner); 
        String passportID = PassportIdValidator.validatePassportId(scanner);
        Color color = ColorValidator.validateColor(scanner);
        Location location = LocationValidator.validateLocation(scanner);
        return new Person(name, coordinates, height, weight, passportID, color, location);
    }
}
