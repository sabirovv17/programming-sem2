package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class Update extends BaseCommand {
    private final InCollectionManager collectionManager;

    public Update(InCollectionManager collectionManager) {
        super("update <id> {person}", "update element by its id");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""}; 
        try {
            if (userCommand[1].isEmpty()) throw new WrongArgumentException();
            long id = Long.parseLong(userCommand[1]);   
            Person person = request.getPerson();
            if (person == null || !person.validate()) throw new WrongArgumentException();
            else {
                if (collectionManager.update(id, person)) {
                    return new Response.ResponseBuilder()
                        .setResponseMessage(String.format("person with id=%d was updated", id))
                        .setExecutionCode(ExecutionCode.OK)
                        .build();
                }
                else {
                    return new Response.ResponseBuilder()
                        .setResponseMessage(String.format("person with id=%d wasn't found", id))
                        .setExecutionCode(ExecutionCode.ERROR)
                        .build();
                }  
            }
        }catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' should has argument(id and new values for object)", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }catch (NumberFormatException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' should has <long> argument", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }

}
