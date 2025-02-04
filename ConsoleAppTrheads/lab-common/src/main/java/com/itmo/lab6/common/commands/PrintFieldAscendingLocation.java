package com.itmo.lab6.common.commands;

import java.util.TreeSet;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCollectionManager;
import com.itmo.lab6.common.interfaces.InDBManager;
import com.itmo.lab6.common.models.Location;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class PrintFieldAscendingLocation extends BaseCommand {
    private final InCollectionManager collectionManager;
    private final InDBManager dbManager;

    public PrintFieldAscendingLocation(InCollectionManager collectionManager, InDBManager dbManager) {
        super("print_field_ascending_location", "show location of all collection elements in ascending order");
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
            TreeSet<Location> locations = new TreeSet<>();
            collectionManager.getCollection()
                .entrySet()
                .stream()
                .forEach(entry -> locations.add(entry.getValue().getLocation()));
            locations.stream().forEach(
                location -> sb.append(location.toString() + "\n")
            );
            return new Response.ResponseBuilder()
                .setResponseMessage(sb.toString())
                .setExecutionCode(ExecutionCode.OK)
                .build();

        }catch(WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' shouldn't has any arguments", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();
        }

    }
}