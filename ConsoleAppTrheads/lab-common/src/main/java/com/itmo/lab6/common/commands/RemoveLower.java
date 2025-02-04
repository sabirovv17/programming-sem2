package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class RemoveLower extends BaseCommand {
    private final InCollectionManager collectionManager;
    private final InDBManager dbManager;

    public RemoveLower(InCollectionManager collectionManager, InDBManager dbManager) {
        super("remove_lower {person}", "removes all elements that lower than current");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""}; 
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            Person person = request.getPerson(); 
            int userId = dbManager.getUserId(request);
            long personId = person.getId();
            collectionManager.setCollection(dbManager.getUserElements(userId));
            if (person == null || !person.validate()) return new Response.ResponseBuilder()
                .setResponseMessage("person is null, nothing to compare")
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
            else if (collectionManager.getCollection().size() == 0) return new Response.ResponseBuilder()
                .setResponseMessage("collection is empty")
                .setExecutionCode(ExecutionCode.OK)
                .build();
            else {
                collectionManager.getCollection().entrySet()
                    .removeIf(entry -> entry.getKey() < person.getId());
                dbManager.removeLower(userId, personId);
                return new Response.ResponseBuilder()
                    .setResponseMessage("all elements lower was deleted")
                    .setExecutionCode(ExecutionCode.OK)
                    .build();
            }
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' shouldn't has any string arguments", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }
}
