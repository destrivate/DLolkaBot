package ru.dev.event;

public class Event {
    private final Events type;

    public Event(Events type){
        this.type = type;
    }

    public Events getType() {
        return type;
    }
}
