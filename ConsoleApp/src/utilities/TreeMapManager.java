package utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import entities.Coordinates;
import entities.Location;
import entities.Person;

import java.io.*;
import java.nio.file.FileSystemException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Collection manager class that provides basic operations for interactions with a TreeMap consisting of Person objects.
 * @see Person
 */
public class TreeMapManager {

    private final TreeMap<Long, Person> personMap;
    private File csvCollection;
    private final LocalDateTime initDate;
    private boolean hasStarted = false;

    {
        personMap = new TreeMap<>();
    }

    /**
     * Class constructor with an initial file specification.
     * @param collectionPath path to the initial CSV file
     * @throws IOException in case of an error while finding the specified file.
     */
    public TreeMapManager(String collectionPath) throws IOException {
        try {
            if (collectionPath == null) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Путь до файла CSV нужно передать через переменную окружения init_file.");
            System.exit(1);
        }
        File file = new File(collectionPath);
        try {
            if (file.exists()) this.csvCollection = new File(collectionPath);
            else throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Файл по указанному пути не существует.");
            System.exit(1);
        }
        this.load();
        this.initDate = LocalDateTime.now();
        hasStarted = true;
    }

    /**
     * Method used during the initial load of the CSV file in the class constructor. Used for adding initial Person
     * values if those exist in a CSV file.
     * @throws IOException if an error while reading the CSV file occurs.
     */
    private void load() throws IOException {
        int beginSize = personMap.size();
        try {
            if (!csvCollection.exists()) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Файла по указанному пути не существует.");
            if (!hasStarted) System.exit(1);
            else return;
        }
        try {
            if (!csvCollection.canRead() || !csvCollection.canWrite()) throw new SecurityException();
        } catch (SecurityException ex) {
            System.out.println("Файл защищён от чтения и/или записи. Для работы программы нужны оба разрешения.");
            if (!hasStarted) System.exit(1);
            else return;
        }
        try {
            if (csvCollection.length() == 0) throw new FileSystemException("");
        } catch (FileSystemException ex) {
            System.out.println("Файл пуст.");
            if (!hasStarted) System.exit(1);
            else return;
        }
        try (CSVReader csvReader = new CSVReader(new FileReader(csvCollection))) {
            System.out.println("Идёт загрузка коллекции " + csvCollection.getAbsolutePath());
            String[] line;
            csvReader.readNext();
            try {
                while ((line = csvReader.readNext()) != null) {
                    long idNum = Long.parseLong(line[0]);
                    Coordinates tempCoord = new Coordinates(0.0, 0);
                    tempCoord.setValFromString(line[2]);
                    LocalDateTime tempDate = LocalDateTime.parse(line[3], Person.formatter);
                    Location tempLoc = new Location(0, 0.0, 0L);
                    tempLoc.setValFromString(line[8]);
                    Person newPerson = new Person(idNum, line[1], tempCoord, tempDate,
                            Long.parseLong(line[4]), Long.parseLong(line[5]), line[6], line[7], tempLoc);
                    personMap.put(idNum, newPerson);
                }
            } catch (Exception e) {
                System.out.println("Ошибка при формировании начальной коллекции.");
                System.exit(1);
            }
            System.out.println("Коллекция успешно загружена. Добавлено " + (personMap.size() - beginSize) + " элементов.");
        } catch (CsvValidationException e) {
            System.out.println("Ошибка при считывании CSV-файла.");
        }
    }

    /**
     * Method used for displaying a list of Person elements in the collection.
     */
    public void show() {
        if (!personMap.isEmpty()) {
            personMap.forEach((k, v) -> System.out.println(v.toString() + "\n"));
        } else System.out.println("Коллекция пуста.");
    }

    /**
     * Method used to insert a Person element into the collection.
     * @param person a Person object to be inserted.
     */
    public void insert(Person person) {
        try {
            personMap.put(person.getId(), person);
            System.out.println("Элемент успешно добавлен в коллекцию.");
        } catch (ClassCastException | NullPointerException e) {
            System.out.println("Не удалось добавить элемент в коллекцию.");
        }
    }

    /**
     * Method used to update a Person element in the collection.
     * @param id id of the element to be updated.
     * @param person an updated Person object.
     */
    public void update(long id, Person person) {
        try {
            person.setID(id);
            Person.counter_id--;
            personMap.put(person.getId(), person);
            System.out.println("Элемент #" + id + " успешно обновлен");
        } catch (ClassCastException | NullPointerException e) {
            System.out.println("Не удалось обновить элемент.");
        }
    }


    /**
     * Method used to remove a Person element from the collection.
     * @param key key of the element that will be deleted.
     */
    public void remove(long key) {
        try {
            personMap.remove(key);
            System.out.println("Элемент #" + key + " успешно удален из коллекции.");
        } catch (ClassCastException | NullPointerException e) {
            System.out.println("Не удалось удалить элемент из коллекции.");
        }
    }

    /**
     * Method used to clear the Person collection.
     */
    public void clear() {
        personMap.clear();
        System.out.println("Коллекция успешно очищена.");
    }

    /**
     * Method used to remove the Person elements lower than the one specified from the collection.
     * @param person Person object for the comparison.
     */
    public void remove_lower(Person person) {
        ArrayList<Long> keys = new ArrayList<>();
        if (!personMap.isEmpty()) {
            try {
                for (Map.Entry<Long, Person> entry : personMap.entrySet()) {
                    if (entry.getValue().compareTo(person) < 0) {
                        keys.add(entry.getKey());
                    }
                }
            } catch (Exception ex) {
                System.out.println("Ошибка при сравнении элементов коллекции.");
            }
            for (Long k : keys) {
                this.remove(k);
            }
            Person.counter_id--;
        } else System.out.println("Коллекция пуста.");
    }

    /**
     * Method used to replace the Person element in the collection with a new one if the new value is greater.
     * @param key key of the element to be replaced.
     * @param person Person object for the comparison.
     */
    public void replace_greater(long key, Person person) {
        if (personMap.get(key).compareTo(person) < 0) {
            person.setID(key);
            this.insert(person);
            Person.counter_id--;
        } else {
            System.out.println("Новое значение не больше имеющегося в коллекции.");
        }
    }

    /**
     * Saves the Person collection to the initial CSV file.
     */
    public void save() {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(csvCollection))) {
            String header = "ID,Name,Coordinates,Creation_date,Height,Weight,Eye_color,Location\n";
            byte[] buffer = header.getBytes();
            outputStream.write(buffer, 0, buffer.length);
            personMap.forEach((k, v) -> {
                byte[] prsBuff = v.toCSV().getBytes();
                try {
                    outputStream.write(prsBuff, 0, prsBuff.length);
                } catch (IOException e) {
                    System.out.println("Не удалось записать элементы коллекции в файл.");
                }
            });
            System.out.println("Коллекция успешно сохранена.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
        }
    }

    /**
     * Method used to check the uniqueness of a passport ID.
     * @param id passport ID to be checked.
     * @return true if the ID is unique.
     */
    public boolean isUniquePassport(String id) {
        if (!personMap.isEmpty()) {
            return personMap.entrySet().stream().noneMatch(v -> v.getValue().getPassportID().equals(id));
        } else return true;
    }

    /**
     * Method used to check if an ID is used in the collection.
     * @param id ID value to be checked.
     * @return true if the ID is present in the collection.
     */
    public boolean hasID(Long id) {
        if (!personMap.isEmpty()) {
            return personMap.entrySet().stream().anyMatch(v -> v.getValue().getId() == id);
        } else return false;
    }

    /**
     * Returns the average double value of all the height parameters of Person objects in the collection.
     */
    public void heightAverage() {
        if (!personMap.isEmpty()) {
            System.out.println(personMap.values().stream().mapToDouble(Person::getHeight).average().orElse(Double.NaN));
        } else System.out.println("Коллекция пуста. Невозможно вычислить среднее значение.");
        ;
    }

    /**
     * Method used to display all Person elements in the collection in ascending order.
     */
    public void print_asc() {
        if (!personMap.isEmpty()) {
            personMap.values().stream().sorted(Person::compareTo).forEach(person ->
                    System.out.println(person + "\n"));
        } else System.out.println("Коллекция пуста.");
    }

    /**
     * Method used to display all Location parameters in the collection in ascending order.
     */
    public void print_asc_loc() {
        if (!personMap.isEmpty()) {
            personMap.values().stream().sorted(Comparator.comparing(Person::getLocation)).forEach(person ->
                    System.out.println("ID: " + person.getId() + ", Локация: "
                            + person.getLocation().toString() + "\n"));
        } else System.out.println("Коллекция пуста.");
    }

    /**
     * @return a String to display information about the collection type, creation date and size.
     */
    @Override
    public String toString() {
        return "Тип коллекции: " + personMap.getClass() +
                "\nДата инициализации: " + initDate +
                "\nКоличество элементов: " + personMap.size();
    }
}
