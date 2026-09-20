package ru.dev.event.bot;

import ru.dev.event.Event;
import ru.dev.event.Events;

public class OnReady extends Event {
    public OnReady(Events type) {
        super(type);
    }
}
