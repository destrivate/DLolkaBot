package ru.dev.command;

import ru.dev.context.MessageContext;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(){
    }

    public void register(Command command){
        this.commands.put(command.getCommand().toLowerCase(), command);
    }

    public void handle(MessageContext messageContext){
        String content = messageContext.getContent();
        if (content == null) return;

        Command command = commands.get(content.toLowerCase());

        if (command != null) {
            command.onCommand(messageContext);
        }

    }
}
