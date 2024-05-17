import utilities.CommandManager;
import utilities.TreeMapManager;

import java.io.IOException;

public class Main {
    /** Main method of the console application.
     * @param args - command line arguments
     * @throws IOException in case of an input/output exception
     */
    public static void main(String[] args) throws IOException {
        CommandManager commandManager = new CommandManager(new TreeMapManager(System.getenv("init_file")));
        try {
            commandManager.interactiveMode();
        } catch (IOException ex){
            System.out.println("Произошла ошибка при работе в интерактивном режиме.");
            System.exit(1);
        }

    }
}