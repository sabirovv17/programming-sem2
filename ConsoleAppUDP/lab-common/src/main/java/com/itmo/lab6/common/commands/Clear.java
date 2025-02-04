package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class Clear extends BaseCommand {
    private final InCollectionManager collectionManager;

    public Clear(InCollectionManager collectionManager) {
        super("clear", "clear collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            if (collectionManager.getCollection().size() == 0) return new Response.ResponseBuilder()
                .setResponseMessage("collection is empty\nthere's nothing to clear")
                .setExecutionCode(ExecutionCode.OK)
                .build();
            else {
                collectionManager.clear(); 
                return new Response.ResponseBuilder()
                    .setResponseMessage("collection was cleared")
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
