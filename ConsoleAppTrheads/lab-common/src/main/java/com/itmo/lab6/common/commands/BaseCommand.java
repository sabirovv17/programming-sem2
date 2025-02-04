package com.itmo.lab6.common.commands;

import com.itmo.lab6.common.interfaces.Describable;
import com.itmo.lab6.common.interfaces.Executable;
import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;


public class BaseCommand implements Describable, Executable {
    private final String name;
    private final String description;

    public BaseCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }
    @Override
    public Response execute(Request request) {
        return null;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description;
    }

    
}
