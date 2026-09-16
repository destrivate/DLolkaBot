package ru.dev.event.message;

import ru.dev.event.Event;
import ru.dev.event.Events;

public class MessageCreate extends Event {
    public MessageCreate(Events type) {
        super(type);
    }
}
