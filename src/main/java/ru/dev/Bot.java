package ru.dev;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import ru.dev.command.CommandManager;
import ru.dev.context.MessageContext;
import ru.dev.event.EventManager;
import ru.dev.event.bot.OnReady;
import ru.dev.event.message.OnMessageCreate;

import java.net.URI;
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
    private final ApiClient apiClient;

    private final EventManager eventManager;


    public Bot(String token) {
        this.token = token;
        this.apiClient = new ApiClient(token);
        this.commandManager = new CommandManager();
        this.eventManager = new EventManager();
    }

    public CommandManager getCommandManager(){return commandManager;}
    public EventManager getEventManager(){return eventManager;}
    public ApiClient getApiClient() {return apiClient;}



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
                    System.out.println(event);

                    if (op == 10 || (event.has("d") && event.getJSONObject("d").has("heartbeat_interval"))) {
                        long interval = event.getJSONObject("d").getLong("heartbeat_interval");
                        startHeartbeat(interval);
                        sendIdentify();
                        return;
                    }
                    if ("READY".equals(t)) {
                        botId = event.getJSONObject("d").getJSONObject("user").getString("id");
                        eventManager.dispatch(new OnReady());
                    } else if ("MESSAGE_CREATE".equals(t)) {
                        JSONObject msg = event.getJSONObject("d");
                        String authorId = msg.getJSONObject("author").getString("id");
                        if (!botId.equals(authorId)) {
                            commandManager.handle(new MessageContext(msg));
                            eventManager.dispatch(new OnMessageCreate(new MessageContext(msg)));
                        }
                    }
                    //eventManager.dispatch(new GlobalEvent());


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
        d.put("intents", 33283);
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
