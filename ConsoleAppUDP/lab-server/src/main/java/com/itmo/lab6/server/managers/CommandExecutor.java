package com.itmo.lab6.server.managers;

import com.itmo.lab6.common.commands.BaseCommand;
import com.itmo.lab6.common.commands.History;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;

public class CommandExecutor {
    private final CommandManager commandManager;

    public CommandExecutor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""}; 
        BaseCommand command = commandManager.getCommandList().get(userCommand[0]);
        if (command == null) {
            return new Response.ResponseBuilder()
                    .setResponseMessage(String.format("unknow command '%s', type 'help' to see all available commands", userCommand[0]))
                    .build();
        }
        Response response = command.execute(request); 
        if (response.getExecutionCode() == ExecutionCode.OK) {
            History.addToHistory(command.getName());
        }
        return response;
    }
}
