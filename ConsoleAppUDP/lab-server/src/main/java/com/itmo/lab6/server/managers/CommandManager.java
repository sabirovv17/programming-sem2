package com.itmo.lab6.server.managers;

import java.util.Map;
import java.util.HashMap;
import com.itmo.lab6.common.commands.*;
import com.itmo.lab6.common.interfaces.InCommandManager;

public class CommandManager implements InCommandManager {
    private final Map<String, BaseCommand> commandList = new HashMap<>();

    public CommandManager(CollectionManager collectionManager) {
        commandList.put("help", new Help(this)); 
        commandList.put("show", new Show(collectionManager));  
        commandList.put("insert", new Insert(collectionManager));  
        commandList.put("update", new Update(collectionManager));  
        commandList.put("remove_key", new RemoveKey(collectionManager));
        commandList.put("clear", new Clear(collectionManager));  
        commandList.put("exit", new Exit());  
        commandList.put("remove_lower", new RemoveLower(collectionManager));
        commandList.put("history", new History());  
        commandList.put("replace_if_greater", new ReplaceIfGreater(collectionManager)); 
        commandList.put("average_of_height", new AverageOfHeight(collectionManager));  
        commandList.put("print_ascending", new PrintAscending(collectionManager));  
        commandList.put("print_field_ascending_location", new PrintFieldAscendingLocation(collectionManager)); 
        commandList.put("info", new Info(collectionManager)); 
    }  

    public Map<String, BaseCommand> getCommandList() {
        return commandList;
    }
}
