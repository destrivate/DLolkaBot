package ru.dev.event;

import org.json.JSONObject;
import ru.dev.event.message.MessageCreate;


public class EventPublisher {
    private GlobalEvent globalEventListener;
    public GlobalEvent getGlobalEventListener() {
        return globalEventListener;
    }
    public interface GlobalEvent {
        void onEvent(JSONObject event);
    }
    public void globalEvent(GlobalEvent listener) {
        this.globalEventListener = listener;
    }

    private MessageCreateEvent messageCreateListener;
    public MessageCreateEvent getMessageCreateListener() {
        return messageCreateListener;
    }
    public interface MessageCreateEvent {
        void onEvent(MessageCreate event);
    }
    public void messageCreateEvent(MessageCreateEvent listener) {
        this.messageCreateListener = listener;
    }


}
