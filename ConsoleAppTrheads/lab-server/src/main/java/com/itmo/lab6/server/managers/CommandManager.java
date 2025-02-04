package com.itmo.lab6.server.managers;

import java.util.Map;
import java.util.HashMap;
import com.itmo.lab6.common.commands.*;
import com.itmo.lab6.common.interfaces.InCommandManager;
import com.itmo.lab6.server.db.DBManager;

public class CommandManager implements InCommandManager {
    private final Map<String, BaseCommand> commandList = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, DBManager dbManager) {
        commandList.put("help", new Help(this)); 
        commandList.put("show", new Show(collectionManager, dbManager));  
        commandList.put("insert", new Insert(collectionManager, dbManager));  
        commandList.put("update", new Update(collectionManager, dbManager));  
        commandList.put("remove_key", new RemoveKey(collectionManager, dbManager));
        commandList.put("clear", new Clear(collectionManager, dbManager));  
        commandList.put("exit", new Exit());  
        commandList.put("remove_lower", new RemoveLower(collectionManager, dbManager));
        commandList.put("history", new History());  
        commandList.put("replace_if_greater", new ReplaceIfGreater(collectionManager, dbManager)); 
        commandList.put("average_of_height", new AverageOfHeight(collectionManager, dbManager));  
        commandList.put("print_ascending", new PrintAscending(collectionManager, dbManager));  
        commandList.put("print_field_ascending_location", new PrintFieldAscendingLocation(collectionManager, dbManager)); 
        commandList.put("info", new Info(collectionManager, dbManager)); 
    }  

    public Map<String, BaseCommand> getCommandList() {
        return commandList;
    }
}
