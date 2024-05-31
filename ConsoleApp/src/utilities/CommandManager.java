package utilities;

import entities.Color;
import entities.Coordinates;
import entities.Location;
import entities.Person;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class used to process user commands to interact with the Person collection.
 * @see Person
 */
public class CommandManager {

    private final TreeMapManager manager;
    private String userCommand;

    private final Deque<String> commandHistory;

    private boolean scriptMode = false;

    private  Scanner scriptReader;

    Scanner commandReader;

    {
        userCommand = "";
        commandReader = new Scanner(System.in);
        commandHistory = new ArrayDeque<>();
    }

    /**
     * Class constructor specifying the TreeMapManager object.
     * @param manager TreeMapManager object used for collection interaction.
     */
    public CommandManager(TreeMapManager manager) {
        this.manager = manager;
    }

    /**
     * Main mode of the application where user input is processed until the 'exit' command is entered.
     * @throws IOException if an exception while processing user inputs occurs.
     */
    public void interactiveMode() throws IOException {
        while (!userCommand.equals("exit")) {
            System.out.print("> ");
            if (!scriptMode) {
                userCommand = commandReader.nextLine();
            } else {
                if (scriptReader.hasNextLine()) {
                    userCommand = scriptReader.nextLine();
                    System.out.println(userCommand);
                } else {
                    scriptMode = false;
                    System.out.println("\b\b");
                    continue;
                }
            }
            if (commandHistory.size() > 10) {
                commandHistory.removeLast();
            }
            String[] finalUserCommand = userCommand.trim().split(" ", 2);
            finalUserCommand[0] = finalUserCommand[0].toLowerCase();
            try {
                switch (finalUserCommand[0]) {
                    case "", "exit" -> {
                    }
                    case "show" -> manager.show();
                    case "help" -> printHelp();
                    case "info" -> System.out.println(manager.toString());
                    case "insert" -> {
                        Person insPerson;
                        if (finalUserCommand.length > 1){
                            insPerson = manager.getElementFromCSV(finalUserCommand[1].split(","));
                        } else{
                            insPerson = getPersonInput();
                        }
                        manager.insert(insPerson);
                    }
                    case "update" -> {
                        long id = Long.parseLong(finalUserCommand[1]);
                        if (manager.hasID(id)) {
                            Person updPerson = getPersonInput();
                            manager.update(id, updPerson);
                        } else {
                            System.out.println("ID не найден в коллекции.");
                        }
                    }
                    case "remove_key" -> {
                        long key = Long.parseLong(finalUserCommand[1]);
                        if (manager.hasID(key)) {
                            manager.remove(key);
                        } else {
                            System.out.println("Ключ не найден в коллекции.");
                        }
                    }
                    case "clear" -> manager.clear();
                    case "save" -> manager.save();
                    case "history" -> {
                        Iterator<String> it = commandHistory.descendingIterator();
                        while (it.hasNext()) {
                            System.out.println(it.next());
                        }
                    }
                    case "remove_lower" -> {
                        Person remPerson = getPersonInput();
                        manager.remove_lower(remPerson);
                    }
                    case "replace_greater" -> {
                        long greaterKey = Long.parseLong(finalUserCommand[1]);
                        if (manager.hasID(greaterKey)) {
                            Person remGPerson = getPersonInput();
                            manager.replace_greater(greaterKey, remGPerson);
                        } else {
                            System.out.println("Ключ не найден в коллекции.");
                        }
                    }
                    case "average_of_height" -> manager.heightAverage();
                    case "print_ascending" -> manager.print_asc();
                    case "print_field_ascending_location" -> manager.print_asc_loc();
                    case "execute_script" -> {
                        if (scriptMode){
                            System.out.println("Невозможно запустить новый скрипт до завершения предыдущего.");
                            break;
                        }
                        try {
                            scriptReader = new Scanner(new File(finalUserCommand[1]));
                            scriptMode = true;
                        } catch (Exception e) {
                            System.out.println("Не удалось найти файл скрипта.");
                        }
                    }
                    default -> System.out.println("Неопознанная команда. Введите 'help' для справки.");
                }
                commandHistory.push(userCommand);
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Отсутствует аргумент.");
            }
        }
    }

    /**
     * Method used to display a list of all commands that a user can enter.
     */
    private static void printHelp() {
        System.out.println("Список доступных команд:");
        System.out.println("help : вывести справку по доступным командам");
        System.out.println("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        System.out.println("show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        System.out.println("insert null {element} : добавить новый элемент с заданным ключом");
        System.out.println("update id {element} : обновить значение элемента коллекции, id которого равен заданному");
        System.out.println("remove_key null : удалить элемент из коллекции по его ключу");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        System.out.println("exit : завершить программу (без сохранения в файл)");
        System.out.println("remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный");
        System.out.println("history : вывести последние 10 команд (без их аргументов)");
        System.out.println("replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого");
        System.out.println("average_of_height : вывести среднее значение поля height для всех элементов коллекции");
        System.out.println("print_ascending : вывести элементы коллекции в порядке возрастания");
        System.out.println("print_field_ascending_location : вывести значения поля location всех элементов в порядке возрастания");
    }

    /**
     * Method used to assemble a Person object from user inputs.
     * @return Person object to be further processed in the interactive mode.
     */
    private Person getPersonInput(){
        try {
            String prsName = getNameInput();
            Coordinates prsCoords = getCoordInput();
            long prsHeight = getHeightInput();
            Long prsWeight = getWeightInput();
            String prsID = getIDInput();
            Color prsEye = getColorInput();
            Location prsLoc = getLocationInput();
            return new Person(prsName, prsCoords, prsHeight, prsWeight, prsID, prsEye, prsLoc);
        } catch (Exception e) {
            System.out.println("Ошибка при создании элемента.");
            return null;
        }
    }

    /**
     * @return the name parameter of a Person object from user input.
     */
    private String getNameInput(){
        String prsName;
        do {
            System.out.println("Введите имя: ");
            prsName = commandReader.nextLine();
        } while (prsName.isEmpty());
        return prsName;
    }

    /**
     * @return the coordinate parameter of a Person object from user input.
     */
    private Coordinates getCoordInput(){
        Coordinates prsCoords = new Coordinates(0.0, 0);
        while (prsCoords != null) {
            System.out.println("Введите координаты в формате \"x;y\": ");
            String strCoords = commandReader.nextLine();
            try {
                prsCoords.setValFromString(strCoords);
            } catch (Exception ex) {
                System.out.println("Неверный формат координат.");
                continue;
            }
            if (prsCoords.getX() > -442 & prsCoords.getY() > -258) {
                break;
            } else {
                System.out.println("Значение x должно быть > -442, значение y > -258.");
            }
        }
        return prsCoords;
    }

    /**
     * @return the height parameter of a Person object from user input.
     */
    private long getHeightInput(){
        long prsHeight = 0;
        while (prsHeight == 0) {
            System.out.println("Введите рост: ");
            String strHeight = commandReader.nextLine();
            try {
                prsHeight = Long.parseLong(strHeight);
            } catch (Exception ex) {
                System.out.println("Неверный формат при вводе роста.");
                continue;
            }
            if (prsHeight < 0) {
                System.out.println("Невозможно использовать отрицательное значение.");
                continue;
            }
            break;
        }
        return prsHeight;
    }

    /**
     * @return the weight parameter of a Person object from user input.
     */
    private Long getWeightInput(){
        Long prsWeight = 0L;
        while (prsWeight == 0L) {
            System.out.println("Введите вес: ");
            String strWeight = commandReader.nextLine();
            if (strWeight.isEmpty()) {
                break;
            }
            try {
                prsWeight = Long.parseLong(strWeight);
            } catch (Exception ex) {
                System.out.println("Неверный формат при вводе веса.");
                continue;
            }
            if (prsWeight < 0) {
                System.out.println("Невозможно использовать отрицательное значение.");
                continue;
            }
            break;
        }
        return prsWeight;
    }

    /**
     * @return the passport ID of a Person object from user input.
     */
    private String getIDInput() {
        String prsID = null;
        while (prsID == null) {
            System.out.println("Введите ID паспорта: ");
            prsID = commandReader.nextLine();
            if (prsID.length() < 5 | prsID.length() > 34 | prsID.isEmpty()) {
                System.out.println("Количество символов: от 5 до 34.");
                continue;
            }
            if (!manager.isUniquePassport(prsID)) {
                System.out.println("ID паспорта не уникально.");
                continue;
            }
            break;
        }
        return prsID;
    }

    /**
     * @return the eye color parameter of a Person object from user input.
     */
    private Color getColorInput() {
        Color prsEye = null;
        while (prsEye == null) {
            System.out.println("Введите цвет глаз (RED, BLACK, WHITE, BLUE, BROWN): ");
            String strEye = commandReader.nextLine();
            if (strEye.isEmpty()) {
                break;
            }
            try {
                prsEye = Color.valueOf(strEye.toUpperCase());
            } catch (IllegalArgumentException ex) {
                System.out.println("Цвет не указан в перечне.");
                continue;
            }
            break;
        }
        return prsEye;
    }

    /**
     * @return the location parameter of a Person object from user input.
     */
    private  Location getLocationInput(){
        Location prsLoc = new Location(0, 0.0, 0L);
        while (prsLoc != null) {
            System.out.println("Введите локацию в формате \"x;y;z\": ");
            String strLoc = commandReader.nextLine();
            try {
                prsLoc.setValFromString(strLoc);
            } catch (Exception ex) {
                System.out.println("Неверный формат локации.");
                continue;
            }
            break;
        }
        return prsLoc;
    }
}