package com.itmo.lab6.common.commands;

import java.util.LinkedList;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class History extends BaseCommand {
    private static final int HISTORY_SIZE = 10;
    private static LinkedList<String> commandQueue = new LinkedList<>();

    public History() {
        super("history", "show last 10 commands(without arguments)");
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[] {request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException();
            return new Response.ResponseBuilder()
                .setResponseMessage(commandQueue.toString())
                .setExecutionCode(ExecutionCode.OK)
                .build(); 
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s shouldn't has any arguments'", getName()))
                .build();
        }
    }

    
    public static void addToHistory(String commandName) {
        if (commandQueue.size() < HISTORY_SIZE) 
            commandQueue.addFirst(commandName);
        else if (commandQueue.size() == HISTORY_SIZE) {
            commandQueue.removeLast(); 
            commandQueue.addFirst(commandName);
        }

    }
    
}
