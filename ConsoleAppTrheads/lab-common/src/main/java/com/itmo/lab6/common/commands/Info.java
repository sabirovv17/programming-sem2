package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.DatabaseInsertionException;
import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;


public class Info extends BaseCommand {
    private final InCollectionManager collectionManager;
    private final InDBManager dbManager;
    public Info(InCollectionManager collectionManager, InDBManager dbManager) {
        super("info", "show info about collection");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override 
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""}; 
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            Integer userId = dbManager.getUserId(request);
            if (userId == null) throw new DatabaseInsertionException();
            StringBuilder sb = new StringBuilder();
            collectionManager.setCollection(dbManager.getAllElementsFromDB());
            sb.append(String.format("total collection size: %d\n", collectionManager.getCollection().size()));
            collectionManager.setCollection(dbManager.getUserElements(userId));
            sb.append(String.format("your collection size: %d\n", collectionManager.getCollection().size()));
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
        } catch (DatabaseInsertionException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage("no user found")
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }
}
