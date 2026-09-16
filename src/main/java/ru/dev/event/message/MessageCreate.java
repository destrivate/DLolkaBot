package ru.dev.event.message;

import ru.dev.context.MessageContext;
import ru.dev.event.Event;
import ru.dev.event.Events;

public class MessageCreate extends Event {
    private MessageContext messageContext;
    public MessageCreate(Events type, MessageContext messageContext) {
        super(type);
        this.messageContext = messageContext;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }
}
