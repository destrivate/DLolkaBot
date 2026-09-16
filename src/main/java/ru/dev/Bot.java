package ru.dev;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import ru.dev.command.CommandManager;
import ru.dev.context.MessageContext;
import ru.dev.event.EventPublisher;
import ru.dev.event.Events;
import ru.dev.event.message.MessageCreate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {
    private final String token;
    private final String gatewayUrl = "wss://lolka.app/ws/bot?v=10&encoding=json";
    private String botId = "";

    private WebSocketClient webSocketClient;
    private ScheduledExecutorService heartbeatScheduler;

    private final CommandManager commandManager;
    private final HttpClient httpClient;
    private final EventPublisher eventPublisher;


    public Bot(String token) {
        this.token = token;
        this.httpClient = HttpClient.newHttpClient();
        this.commandManager = new CommandManager();
        this.eventPublisher = new EventPublisher();
    }

    public CommandManager getCommandManager(){return commandManager;}
    public EventPublisher getEventPublisher(){return eventPublisher;}

    public void sendMessage(String channelId, String textContent) {
        CompletableFuture.runAsync(() -> {
            try {
                String url = "https://lolka.app/api/bot/v10/channels/" + channelId + "/messages";

                JSONObject payload = new JSONObject();
                payload.put("content", textContent);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization", "Bot " + token)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                        .build();

                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {

            }
        });
    }


    public void start(){
        connectWebSocket();
    }

    private void connectWebSocket() {
        try {
            webSocketClient = new WebSocketClient(new URI(gatewayUrl)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                }

                @Override
                public void onMessage(String message) {
                    JSONObject event = new JSONObject(message);
                    int op = event.optInt("op", -1);
                    String t = event.optString("t", "NONE");

                    System.out.println("[<-] Event (Opcode " + op + "): " + t);

                    if (op == 10 || (event.has("d") && event.getJSONObject("d").has("heartbeat_interval"))) {
                        long interval = event.getJSONObject("d").getLong("heartbeat_interval");
                        startHeartbeat(interval);
                        sendIdentify();
                        return;
                    }



                    if ("READY".equals(t)) {
                        botId = event.getJSONObject("d").getJSONObject("user").getString("id");
                    } else if ("MESSAGE_CREATE".equals(t)) {
                        JSONObject msg = event.getJSONObject("d");
                        String authorId = msg.getJSONObject("author").getString("id");

                        if (!botId.equals(authorId)) {
                            commandManager.handle(new MessageContext(msg));
                            if (eventPublisher.getMessageCreateListener() != null){
                                eventPublisher.getMessageCreateListener().onEvent(new MessageCreate(Events.MESSAGE_CREATE));
                            }
                        }


                    }

                    if (eventPublisher.getGlobalEventListener() != null){
                        eventPublisher.getGlobalEventListener().onEvent(event);
                    }


                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    stopHeartbeat();
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            webSocketClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendIdentify() {
        JSONObject identify = new JSONObject();
        identify.put("op", 2);

        JSONObject d = new JSONObject();
        d.put("token", token);
        d.put("intents", 33280);
        d.put("capabilities", 16383);

        JSONObject properties = new JSONObject();
        properties.put("os", "linux");
        properties.put("browser", "linux");
        properties.put("device", "linux");
        d.put("properties", properties);

        JSONObject presence = new JSONObject();
        presence.put("status", "online");
        presence.put("afk", false);
        d.put("presence", presence);

        identify.put("d", d);

        webSocketClient.send(identify.toString());
    }


    private void startHeartbeat(long intervalMs) {
        stopHeartbeat();
        heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            try {
                if (webSocketClient != null && webSocketClient.isOpen()) {
                    JSONObject heartbeat = new JSONObject();
                    heartbeat.put("op", 1);
                    heartbeat.put("d", JSONObject.NULL);
                    webSocketClient.send(heartbeat.toString());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, intervalMs, intervalMs, TimeUnit.MILLISECONDS);
    }

    private void stopHeartbeat() {
        if (heartbeatScheduler != null && !heartbeatScheduler.isShutdown()) {
            heartbeatScheduler.shutdownNow();
        }
    }
}
