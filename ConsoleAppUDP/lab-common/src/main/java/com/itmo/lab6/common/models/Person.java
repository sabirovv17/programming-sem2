package com.itmo.lab6.common.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.itmo.lab6.common.util.IdGenerator;

public class Person implements Comparable<Person>, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long height; //Значение поля должно быть больше 0
    private Long weight; //Поле может быть null, Значение поля должно быть больше 0
    private String passportID; //Длина строки должна быть не меньше 5, Длина строки не должна быть больше 34, Поле не может быть null
    private Color color; //Поле может быть null
    private Location location; //Поле не может быть null
    
    public Person(String name, Coordinates coordinates, LocalDateTime creationDate, long height, Long weight, String passportID, Color color, Location location) {
        this.id = IdGenerator.generateUniqueRandomId(); 
        this.name = name; 
        this.coordinates = coordinates; 
        this.creationDate = creationDate; 
        this.height = height;
        this.weight = weight; 
        this.passportID = passportID;
        this.color = color;
        this.location = location;
    }
    public Person(long id, String name, Coordinates coordinates, LocalDateTime creationTime, long height, Long weight, String passportID, Color color, Location location) {
        this.id = id; 
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationTime;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID; 
        this.color = color;
        this.location = location;
    }


    public Person(String name, Coordinates coordinates, long height, Long weight, String passportID, Color color, Location location) {
        this.id = IdGenerator.generateUniqueRandomId(); 
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
        this.color = color;
        this.location = location;
    }
    public Person(String[] args) {
        this.id = Long.parseLong(args[0]); 
        this.name = args[1];
        this.coordinates = new Coordinates(Double.parseDouble(args[2]), Integer.parseInt(args[3]));
        this.creationDate = LocalDateTime.now();
        this.height = Long.parseLong(args[4]); 
        this.weight = Long.parseLong(args[5]); 
        this.passportID = args[6]; 
        this.color = Color.valueOf(args[7].toUpperCase());
        this.location = new Location(Float.parseFloat(args[8]), Double.parseDouble(args[9]), Long.parseLong(args[10]));
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public Coordinates getCoordinates() {return coordinates;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public long getHeight() {return height;}
    public Long getWeight() {return weight;}
    public String getPassportID() {return passportID;}
    public Color getColor() {return color;}
    public Location getLocation() {return location;}

    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setCoordinates(Coordinates coordinates) {this.coordinates = coordinates;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public void setHeight(long height) {this.height = height;}
    public void setWeight(Long weight) {this.weight = weight;}
    public void setPassportID(String passportID) {this.passportID = passportID;}
    public void setColor(Color color) {this.color = color;}
    public void setLocaltion(Location location) {this.location = location;}

    @Override
    public int compareTo(Person person) {
        int comparisonSum = 0;
        comparisonSum += Long.compare(this.id, person.id);
        comparisonSum += this.name.compareToIgnoreCase(person.name);
        comparisonSum += this.coordinates.compareTo(person.coordinates);
        comparisonSum += this.creationDate.compareTo(person.creationDate);
        comparisonSum += Long.compare(this.height, person.height);
        comparisonSum += Long.compare(this.weight, person.weight); 
        comparisonSum += this.passportID.compareToIgnoreCase(person.passportID); 
        comparisonSum += this.location.compareTo(person.location);

        return comparisonSum;
    }
    public boolean validate() {
        if (id < 0) return false; 
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (height < 0) return false;
        if (weight < 0 || weight == null) return false;
        if (passportID.length() < 5 || passportID.length() > 34 || passportID == null) return false;
        if (color == null) return false;
        if (location == null) return false;
        return true;
    }


    @Override
    public String toString() {
        return "ID: " + id + ", " +
                "name: " + name + ", " +
                "coordinates: " + coordinates.getX() + "; " + coordinates.getY() + ", " +
                "creation date: " + creationDate + ",\n" +
                "height: " + height + ", " +
                "weight: " + weight + ", " +
                "passport ID: " + passportID + ", " +
                "eye color: " + color +", " +
                "location: " + location.toString();
    }
    public String[] toCSV() {
        return new String[]{
            String.valueOf(id), 
            name, 
            coordinates.toString(), 
            creationDate.toString(),
            String.valueOf(height), 
            String.valueOf(weight), 
            passportID, 
            color.toString(), 
            location.forCSV()
        };
    }
}
