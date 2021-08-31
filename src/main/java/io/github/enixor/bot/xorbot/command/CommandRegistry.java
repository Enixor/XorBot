package io.github.enixor.bot.xorbot.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command> commandMap = new HashMap<>();

    public void register(Command command) {
        this.commandMap.put(command.getName(), command);
    }

    public Map<String, Command> getCommandMap() {
        return this.commandMap;
    }

}