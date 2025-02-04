package com.itmo.lab6.server.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.Map;

import com.itmo.lab6.common.models.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CSVManager {
    public static void load(String filePath, CollectionManager collectionManager) {
        try {
            if (filePath == null) throw new FileNotFoundException();
        } catch (FileNotFoundException exception) {
            System.err.println("cannot find file"); 
            System.exit(1);
        }
        File file = new File(filePath);
        try {
            if (!file.exists()) throw new FileNotFoundException();
            if (!file.canRead() || !file.canWrite()) throw new SecurityException();
        } catch (FileNotFoundException exception) {
            System.err.println("cannot find file");
        } catch (SecurityException exception) {
            System.err.println("unable to read/write file");
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                long id = Long.parseLong(line[0]);
                String name = line[1];
                String coords = line[2];
                Coordinates coordinates = new Coordinates(Double.parseDouble(coords.split(":", 2)[0]), Integer.parseInt(coords.split(":", 2)[1]));
                LocalDateTime creationDate = LocalDateTime.parse(line[3]);
                long height = Long.parseLong(line[4]); 
                Long weight = Long.parseLong(line[5]); 
                String passportID = line[6]; 
                Color color = Color.valueOf(line[7].toUpperCase()); 
                String loc = line[8]; 
                Location location = new Location(Float.parseFloat(loc.split(":", 3)[0]), Double.parseDouble(loc.split(":", 3)[1]), Long.parseLong(loc.split(":", 3)[2]));
                Person person = new Person(id, name, coordinates, creationDate, height, weight, passportID, color, location);
                collectionManager.addElement(person); 
            }
        } catch (IOException | CsvValidationException | NumberFormatException exception) {
            System.exit(1);
        }
    }
    public static void writeCollection(String filePath, CollectionManager collectionManager) {
       try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeNext(new String[]{"ID", "Name", "Coordinates", "Creation Date", "Height", "Weight", "Passport ID", "Color", "Location"});
            for (Map.Entry<Long, Person> entry : collectionManager.getCollection().entrySet()) {
                Person person = entry.getValue();
                writer.writeNext(person.toCSV());
            }
       }  catch (IOException exception) {
       }
    }
}
