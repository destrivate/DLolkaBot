package ru.dev.event.bot;

import ru.dev.event.Event;
import ru.dev.event.Events;

public class Ready extends Event {
    public Ready(Events type) {
        super(type);
    }
}
