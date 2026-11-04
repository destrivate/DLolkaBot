package ru.dev.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventManager {
    private Map<Class<?>, Consumer<?>> listeners = new HashMap<>();

    public <T> void on(Class<T> event,Consumer<T> listener){
        listeners.put(event,listener);
    }

    public <T> void dispatch(T event){
        Consumer<T> listener = (Consumer<T>) listeners.get(event);

        if (listener != null){
            listener.accept(event);
        }
    }
}
