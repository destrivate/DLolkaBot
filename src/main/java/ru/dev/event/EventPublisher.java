package ru.dev.event;

import org.json.JSONObject;
import ru.dev.event.bot.*;
import ru.dev.event.message.*;
import ru.dev.event.user.*;


public class EventPublisher {
    private OnGlobalEvent onGlobalEventListener;
    public OnGlobalEvent getOnGlobalEventListener() {
        return onGlobalEventListener;
    }
    public interface OnGlobalEvent {
        void onEvent(JSONObject event);
    }
    public void onGlobalEvent(OnGlobalEvent listener) {this.onGlobalEventListener = listener;}

    private OnMessageCreateEvent onMessageCreateListener;
    public OnMessageCreateEvent getOnMessageCreateListener() {
        return onMessageCreateListener;
    }
    public interface OnMessageCreateEvent {
        void onEvent(OnMessageCreate event);
    }
    public void onMessageCreateEvent(OnMessageCreateEvent listener) {this.onMessageCreateListener = listener;}


    private OnReadyEvent onReadyEventListener;
    public OnReadyEvent getReadyEventListener() {
        return onReadyEventListener;
    }
    public interface OnReadyEvent {
        void onEvent(OnReady event);
    }
    public void onReadyEvent(OnReadyEvent listener) {this.onReadyEventListener = listener;}


    private OnJoinEvent onJoinEvent;
    public OnJoinEvent getOnJoinEvent() {
        return onJoinEvent;
    }
    public interface OnJoinEvent {
        void onEvent(OnJoin event);
    }
    public void onJoinEvent(OnJoinEvent listener) {
        this.onJoinEvent = listener;
    }



}
