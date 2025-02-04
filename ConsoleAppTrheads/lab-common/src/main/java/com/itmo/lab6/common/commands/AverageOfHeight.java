package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class AverageOfHeight extends BaseCommand {
    private final InCollectionManager collectionManager;
    private final InDBManager dbManager;

    public AverageOfHeight(InCollectionManager collectionManager, InDBManager dbManager) {
        super("average_of_height", "show average of height among all elements");
        this.collectionManager = collectionManager;        
        this.dbManager = dbManager;
    }
    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[] {request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            collectionManager.setCollection(dbManager.getAllElementsFromDB());
            if (collectionManager.getCollection().size() == 0) return new Response.ResponseBuilder()
                    .setResponseMessage("collection is empty")
                    .setExecutionCode(ExecutionCode.OK)
                    .build();
            long average_of_height = collectionManager.getCollection().entrySet()
                .stream()
                .mapToLong(entry -> entry.getValue().getHeight())
                .sum();
            return new Response.ResponseBuilder() 
                .setResponseMessage(String.format("average_of_height: %d", average_of_height / collectionManager.getCollection().size()))
                .setExecutionCode(ExecutionCode.OK)
                .build();
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
            .setResponseMessage(String.format("command '%s shouldn't has any arguments'", getName()))
            .build();
        }
    }
}
