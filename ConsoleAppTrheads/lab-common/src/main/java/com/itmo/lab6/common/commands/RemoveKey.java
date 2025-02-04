package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class RemoveKey extends BaseCommand { 
    private final InCollectionManager collectionManager; 
    private final InDBManager dbManager;

    public RemoveKey(InCollectionManager collectionManager, InDBManager dbManager) {
        super("remove_key <key>", "remove element from the collection by its key/id");
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
            if (collectionManager.remove(personId)) {
                dbManager.removeByKey(userId, personId);
                return new Response.ResponseBuilder()
                    .setResponseMessage(String.format("user with id=%d was removed", personId))
                    .setExecutionCode(ExecutionCode.OK)
                    .build();
            } else return new Response.ResponseBuilder()
                .setResponseMessage(String.format("user with id=%d wasn't found", personId))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command %s should has argument", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }catch (NumberFormatException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command %s should has <long> argument", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }
    }


    // @Override
    // public Response execute(Request request) {
    //     String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""};
    //     try {
    //         if (userCommand[1].isEmpty()) throw new WrongArgumentException();
    //         long id = Long.parseLong(userCommand[1]);
    //         if (collectionManager.remove(id)) {
    //             return new Response.ResponseBuilder()
    //                 .setResponseMessage(String.format("user with id=%d was removed", id))
    //                 .setExecutionCode(ExecutionCode.OK)
    //                 .build();
    //         } else return new Response.ResponseBuilder()
    //             .setResponseMessage(String.format("user with id=%d wasn't found", id))
    //             .setExecutionCode(ExecutionCode.ERROR)
    //             .build();
    //     }catch (WrongArgumentException exception) {
    //         return new Response.ResponseBuilder()
    //             .setResponseMessage(String.format("command %s should has argument", getName()))
    //             .setExecutionCode(ExecutionCode.ERROR)
    //             .build();
    //     }catch (NumberFormatException exception) {
    //         return new Response.ResponseBuilder()
    //             .setResponseMessage(String.format("command %s should has <long> argument", getName()))
    //             .setExecutionCode(ExecutionCode.ERROR)
    //             .build();
    //     }
    // }
}
