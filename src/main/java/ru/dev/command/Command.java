package ru.dev.command;

import ru.dev.context.MessageContext;

public class Command {
    private final String command;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void onCommand(MessageContext messageContext){}
}
