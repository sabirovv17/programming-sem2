package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;

public class PrintAscending extends BaseCommand {
    private final InCollectionManager collectionManager;
    private final InDBManager dbManager;

    public PrintAscending(InCollectionManager collectionManager, InDBManager dbManager) {
        super("print_ascending", "show all collection elements in ascending order");
        this.collectionManager = collectionManager; 
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException(); 
            StringBuilder sb = new StringBuilder();
            collectionManager.setCollection(dbManager.getAllElementsFromDB());
            collectionManager.getCollection()
                .entrySet()
                .stream()
                .forEach(entry -> sb.append(String.format("%d:%s", entry.getKey(), entry.getValue().toString())));
            return new Response.ResponseBuilder()
                .setResponseMessage(sb.toString())
                .setExecutionCode(ExecutionCode.OK)
                .build();
        }catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' shouldn't has any arguments", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }
}