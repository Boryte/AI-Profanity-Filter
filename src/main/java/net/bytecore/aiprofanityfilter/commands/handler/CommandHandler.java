package net.bytecore.aiprofanityfilter.commands.handler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandHandler {

    public CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
    }

    public void registerCommand(CommandMap commandMap, String name, Command command) {
        commandMap.register(name, command);
    }
}
