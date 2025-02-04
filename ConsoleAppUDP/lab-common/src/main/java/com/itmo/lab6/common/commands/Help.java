package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.exceptions.WrongArgumentException;
import com.itmo.lab6.common.interfaces.InCommandManager;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;

public class Help extends BaseCommand {
    private final InCommandManager commandManager;

    public Help(InCommandManager commandManager) {
        super("help", "show list of available commands");
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) {
        String[] userCommand = request.getCommand().contains(" ") ? request.getCommand().split(" ", 2) : new String[]{request.getCommand(), ""};
        try {
            if (!userCommand[1].isEmpty()) throw new WrongArgumentException(); 
            StringBuilder sb = new StringBuilder();

            commandManager.getCommandList().values().forEach(command -> {
                String tableRow = String.format("%-50s%-5s%n", command.getName(), command.getDescription());
                sb.append(tableRow);
            });
            return new Response.ResponseBuilder() 
                    .setResponseMessage(sb.toString())
                    .setExecutionCode(ExecutionCode.OK)
                    .build();
        } catch (WrongArgumentException exception) {
            return new Response.ResponseBuilder()
                .setResponseMessage(String.format("command '%s' shouldn't has any arguments", getName()))
                .setExecutionCode(ExecutionCode.ERROR)
                .build();    
        }
    }
}
