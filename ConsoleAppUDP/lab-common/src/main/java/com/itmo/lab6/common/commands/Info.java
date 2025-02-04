package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;


public class Info extends BaseCommand {
    private final InCollectionManager collectionManager;
    
    public Info(InCollectionManager collectionManager) {
        super("info", "show info about collection");
        this.collectionManager = collectionManager;
    }

    @Override 
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""}; 
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            StringBuilder sb = new StringBuilder();
            String lastInitTime = collectionManager.getLastInitTime() == null ? "no inits in this session\n" : String.format("lastInitTime: %s\n", collectionManager.getLastInitTime());
            sb.append(lastInitTime); 
            String lastSaveTime = collectionManager.getLastSaveTime() == null ? "no savings in this session\n" : String.format("lastSaveTime: %s\n", collectionManager.getLastSaveTime());
            sb.append(lastSaveTime);
            sb.append(String.format("collection size: %s\n", collectionManager.getCollection().size()));
            sb.append(String.format("collection type: %s", collectionManager.getCollection().getClass()));
            return new Response.ResponseBuilder()
                    .setResponseMessage(sb.toString())
                    .setExecutionCode(ExecutionCode.OK)
                    .build();
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                    .setResponseMessage(String.format("Command '%s' shouldn't has any arguments", getName()))
                    .setExecutionCode(ExecutionCode.ERROR)
                    .build();
        }
    }
}
