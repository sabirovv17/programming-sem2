package com.itmo.lab6.common.interfaces;
import java.util.Map; 
import com.itmo.lab6.common.commands.BaseCommand;;

public interface InCommandManager {
    Map<String, BaseCommand> getCommandList();
}
