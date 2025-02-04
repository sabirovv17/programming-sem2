package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.models.Person;

public class ReplaceIfGreater extends BaseCommand {
    private final InCollectionManager collectionManager;

    public ReplaceIfGreater(InCollectionManager collectionManager) {
        super("replace_if_greater <id> {person}", "replace element by id if its value greater"); 
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[] {request.getCommand(), ""};
        try {
            if (userCommand[1].isEmpty()) throw new WrongArgumentException();
            long id = Long.parseLong(userCommand[1]); 
            Person person = request.getPerson();
            if (person == null || !person.validate()) throw new WrongArgumentException();
            else {
                Person currentPerson = collectionManager.getCollection().get(id);
                if (person.compareTo(currentPerson) > 0) {
                    collectionManager.getCollection().replace(id, currentPerson, person);
                    return new Response.ResponseBuilder()
                        .setResponseMessage(String.format("person with id=%d was removed", id))
                        .setExecutionCode(ExecutionCode.OK)
                        .build();
                }    
                else return new Response.ResponseBuilder()
                    .setResponseMessage(String.format("person with id=%d wasan't removed", id))
                    .setExecutionCode(ExecutionCode.ERROR)
                    .build();
            }
        }catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' should has argument!", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }catch (NumberFormatException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage("id value should be <long>")
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }
}