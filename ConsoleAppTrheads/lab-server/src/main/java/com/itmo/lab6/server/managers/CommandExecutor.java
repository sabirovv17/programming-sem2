package com.itmo.lab6.server.managers;

import com.itmo.lab6.common.commands.BaseCommand;
import com.itmo.lab6.common.commands.History;
import com.itmo.lab6.common.util.ExecutionCode;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.RequestCode;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.server.db.DBManager;

public class CommandExecutor {
    private final CommandManager commandManager;
    private final DBManager dbManager;

    public CommandExecutor(CommandManager commandManager, DBManager dbManager) {
        this.commandManager = commandManager;
        this.dbManager = dbManager;
    }

    public Response execute(Request request) {
        if (request.getRequestCode().equals(RequestCode.LOGIN) || request.getRequestCode().equals(RequestCode.REGISTRATION)) {
            boolean isInserted = false;
            if (request.getRequestCode().equals(RequestCode.LOGIN)) {
                Integer userId = dbManager.getUserId(request);
                if (userId != null) isInserted = true;
            } else {
                dbManager.insertNewUser(request);
                isInserted = true;
            }
            if (!isInserted) {
                return new Response.ResponseBuilder()
                    .setExecutionCode(ExecutionCode.REGISTRATION_FAIL)
                    .build();
            }else {
                return new Response.ResponseBuilder()
                    .setExecutionCode(ExecutionCode.REGISTRATION_SUCCESS)
                    .build();
            }
        } else {
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
}
