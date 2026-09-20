package ru.dev.event.user;

import ru.dev.event.Event;
import ru.dev.event.Events;

public class OnJoin extends Event {
    public OnJoin(Events type) {
        super(type);
    }
}
