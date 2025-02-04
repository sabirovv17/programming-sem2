package entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * Class Person represents a person through a series of parameters, such as their height, weight, eye color, passport ID etc.
 */
public class Person implements Comparable<Person>{
    private Collection<Person> collection;
    public static long counter_id = 1;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name; //Поле не может быть null, Строка не может быть пустой
    private final entities.Coordinates coordinates; //Поле не может быть null
    private final java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final long height; //Значение поля должно быть больше 0
    private final Long weight; //Поле может быть null, Значение поля должно быть больше 0
    private final String passportID; //Длина строки должна быть не меньше 5, Значение этого поля должно быть уникальным, Длина строки не должна быть больше 34, Поле не может быть null
    private entities.Color eyeColor; //Поле может быть null
    private final entities.Location location; //Поле не может быть null
    public final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Class constructor in case of creation through user input (id and creationDate are generated automatically).
     * @param name person's name
     * @param coordinates person's coordinates
     * @param height person's height parameter
     * @param weight person's weight parameter
     * @param passportID unique passport ID of a person
     * @param eyeColor person's eye color parameter from preset values
     * @param location location parameter of a person
     */
    public Person(String name, entities.Coordinates coordinates, long height, Long weight, String passportID,
                  entities.Color eyeColor, entities.Location location) {

        this.coordinates = coordinates != null ? coordinates : new entities.Coordinates(0.0, 0);
        this.creationDate = LocalDateTime.now();
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.location = location;
        this.id = ++counter_id;
        this.name = (name != null && !name.isEmpty()) ? name : "Unknown";
    }

    /**
     * Class constructor in case of loading preexisting elements from a file (id and creationDate values are taken
     * directly).
     * @param id ID parameter (normally generated automatically).
     * @param name person's name
     * @param coordinates person's coordinates
     * @param creationDate creation date parameter (normally generated automatically)
     * @param height person's height parameter
     * @param weight person's weight parameter
     * @param passportID unique passport ID of a person
     * @param eyeColor person's eye color parameter from preset values
     * @param location location parameter of a person
     */
    public Person(long id, String name, entities.Coordinates coordinates, java.time.LocalDateTime creationDate, long height, Long weight, String passportID,
                  String eyeColor, entities.Location location) {
        this.id = id;
        if (id > counter_id)
        {
            counter_id = id + 1;
        }
        this.coordinates = coordinates != null ? coordinates : new entities.Coordinates(0.0, 0);
        this.creationDate = creationDate;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
        if(!eyeColor.equals("null")){
            this.eyeColor = Color.valueOf(eyeColor);
        }
        this.location = location;
        this.name = (name != null && !name.isEmpty()) ? name : "Unknown";
    }

    /**
     * Validates passport ID value for length.
     * @param passportID unique passport ID of a person
     * @return true if passport ID is valid.
     */
    private boolean validatePassportID(String passportID) {
        return passportID != null && passportID.length() >= 5 && passportID.length() <= 34;
    }

    /**
     * @return ID of a person collection element
     */
    public long getId() {
        return id;
    }

    /**
     * @param newID new ID value to be set for a collection element
     */
    public void setID(long newID) {
        id = newID;
    }

    /**
     * @return person's height value
     */
    public long getHeight() {
        return height;
    }

    /**
     * @return person's passport ID
     */
    public String getPassportID() {
        return passportID;
    }

    /**
     * @return a collection element's location field
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Compares two Person objects.
     * @param p  the Person object to be compared.
     * @return sum of results of Long value comparisons for height and weight
     */
    @Override
    public int compareTo(Person p) {
        return weight.compareTo(p.weight) + Long.compare(height, p.height);
    }

    /**
     * @return CSV representation of a Person object.
     */
    public String toCSV(){
        return id + "," +
                name + "," +
                coordinates.getX() + ";" + coordinates.getY() + "," +
                creationDate.format(formatter)  + "," +
                height + "," +
                weight + "," +
                passportID + "," +
                eyeColor +"," +
                location.toString() + "\n";
    }

    /**
     * @return String representation of a Person object.
     */
    @Override
    public String toString() {
        return "ID: " + id + ", " +
                "Имя: " + name + ", " +
                "Координаты: " + coordinates.getX() + "; " + coordinates.getY() + ", " +
                "Дата создания: " + creationDate.format(formatter) + ",\n" +
                "Рост: " + height + ", " +
                "Вес: " + weight + ", " +
                "ID паспорта: " + passportID + ", " +
                "Цвет глаз: " + eyeColor +", " +
                "Координаты: " + location.toString();


    }
}

