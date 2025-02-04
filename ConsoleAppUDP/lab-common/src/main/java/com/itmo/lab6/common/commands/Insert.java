package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class Insert extends BaseCommand {
    private final InCollectionManager collectionManager; 

    public Insert(InCollectionManager collectionManager) {
        super("insert {person}", "add new element to the collection");
        this.collectionManager = collectionManager;
    }

    @Override 
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            Person person = request.getPerson();
            if (person == null) return new Response.ResponseBuilder()
                .setResponseMessage("no person was sent")
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
            else {
                if (person.validate()) {
                    collectionManager.addElement(person);
                    return new Response.ResponseBuilder()
                        .setResponseMessage("new person was added to the collection")
                        .setExecutionCode(ExecutionCode.OK)
                        .build();
                }
                return new Response.ResponseBuilder()
                    .setResponseMessage("person isn't valid")
                    .setExecutionCode(ExecutionCode.ERROR)
                    .build();
            }
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setExecutionCode(ExecutionCode.ERROR)
                .setResponseMessage(String.format("command '%s' shouldn't has string arguments", getName()))
                .build();
        }
    }

}
