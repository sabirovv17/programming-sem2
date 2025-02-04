package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class Show extends BaseCommand {
    private final InCollectionManager collectionManager; 
    private final InDBManager dbManager;

    public Show(InCollectionManager collectionManager, InDBManager dbManager) {
        super("show", "show all collection elements");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[] {request.getCommand(), ""}; 
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            collectionManager.setCollection(dbManager.getAllElementsFromDB());
            if (collectionManager.getCollection().size() == 0) return new Response.ResponseBuilder().setResponseMessage("collection is empty").setExecutionCode(ExecutionCode.OK).build();
            else {
                StringBuilder sb = new StringBuilder();
                collectionManager.getCollection().entrySet()
                    .stream()
                    .forEach(entry -> sb.append(String.format("%d:%s\n", entry.getKey(), entry.getValue().toString())));
                    return new Response.ResponseBuilder()   
                        .setResponseMessage(sb.toString())
                        .setExecutionCode(ExecutionCode.OK)
                        .build();
                }
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' shouldn't has arguments", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }
    
}
