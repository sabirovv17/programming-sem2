package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class Update extends BaseCommand {
    private final InCollectionManager collectionManager;
    private final InDBManager dbManager;

    public Update(InCollectionManager collectionManager, InDBManager dbManager) {
        super("update <id> {person}", "update element by its id");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""}; 
        try {
            if (userCommand[1].isEmpty()) throw new WrongArgumentException();
            long personId = Long.parseLong(userCommand[1]);   
            int userId = dbManager.getUserId(request);
            collectionManager.setCollection(dbManager.getUserElements(userId));
            Person person = request.getPerson();
            if (person == null || !person.validate()) throw new WrongArgumentException();
            else {
                if (collectionManager.update(personId, person)) {
                    dbManager.removeByKey(userId, personId);
                    int coordinatesId = dbManager.insertNewCoordinates(person.getCoordinates()); 
                    int locationId = dbManager.insertNewLocation(person.getLocation());
                    dbManager.insertPerson(person, coordinatesId, locationId, userId);
                    return new Response.ResponseBuilder()
                        .setResponseMessage(String.format("person with id=%d was updated", personId))
                        .setExecutionCode(ExecutionCode.OK)
                        .build();
                }
                else {
                    return new Response.ResponseBuilder()
                        .setResponseMessage(String.format("person with id=%d wasn't found", personId))
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
