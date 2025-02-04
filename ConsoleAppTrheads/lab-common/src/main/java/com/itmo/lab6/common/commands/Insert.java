package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.DatabaseInsertionException;
import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.models.Coordinates;
import com.itmo.lab6.common.models.Location;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class Insert extends BaseCommand {
    private final InCollectionManager collectionManager; 
    private final InDBManager dbManager;

    public Insert(InCollectionManager collectionManager, InDBManager dbManager) {
        super("insert {person}", "add new element to the collection");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override 
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            Person person = request.getPerson();
            if (person == null) throw new WrongArgumentException();
            else {
                if (person.validate()) {
                    try {
                        Coordinates coordinates = person.getCoordinates();
                        Location location = person.getLocation();
                        Integer userId = dbManager.getUserId(request); 
                        Integer coordinatesId = dbManager.insertNewCoordinates(coordinates); 
                        Integer locationId = dbManager.insertNewLocation(location); 
                        if (userId == null || coordinatesId == null || locationId == null) throw new DatabaseInsertionException();
                        Integer isInserted = dbManager.insertPerson(person, coordinatesId, locationId, userId);
                        if (isInserted != null) {
                            person.setId(isInserted); 
                            collectionManager.addElement(person);
                            return new Response.ResponseBuilder()
                                .setResponseMessage("new person was inserted")
                                .setExecutionCode(ExecutionCode.OK)
                                .build();
                        } else return new Response.ResponseBuilder()
                            .setResponseMessage("new person wasn't added")
                            .setExecutionCode(ExecutionCode.ERROR)
                            .build();
                    } catch (DatabaseInsertionException exception) {
                        return new Response.ResponseBuilder()
                            .setExecutionCode(ExecutionCode.ERROR)
                            .build();
                    }
                }
            }


        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setExecutionCode(ExecutionCode.ERROR)
                .setResponseMessage(String.format("command '%s' shouldn't has string arguments", getName()))
                .build();
        }
        return new Response.ResponseBuilder()
            .setExecutionCode(ExecutionCode.ERROR)
            .build();
    }

}
