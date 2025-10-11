package dev.xernas.lithium.plugin.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    Map<String, Command> commands = new HashMap<>();

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void clearCommands() {
        commands.clear();
    }

}
