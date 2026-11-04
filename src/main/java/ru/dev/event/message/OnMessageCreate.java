package ru.dev.event.message;

import ru.dev.context.MessageContext;
import ru.dev.event.Event;

public class OnMessageCreate extends Event {
    private MessageContext messageContext;
    public OnMessageCreate(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }
}
