package ru.dev;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private final HttpClient httpClient;
    private final String urlApi = "https://lolka.app/api/bot/v10";
    private final String token;

    public ApiClient(String token) {
        this.httpClient = HttpClient.newHttpClient();
        this.token = token;
    }

    private HttpResponse<String> request(String method, String path, JSONObject body) {
        try {
            String url = urlApi + path;

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bot " + token)
                    .header("Content-Type", "application/json");

            if (body != null) {
                builder.method(method, HttpRequest.BodyPublishers.ofString(body.toString()));
            } else {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            }

            return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String channelId, String textContent) {
        JSONObject payload = new JSONObject();
        payload.put("content", textContent);
        request("POST", "/channels/" + channelId + "/messages", payload);
    }


    public JSONObject getChannel(String channelId) {
        return new JSONObject(request("GET", "/channels/" + channelId, null).body());
    }

    public JSONObject createChannel(String guildId, String name, int type, String parentId, String topic) {
        JSONObject payload = new JSONObject();
        payload.put("name", name);
        payload.put("type", type);
        if (parentId != null) payload.put("parent_id", parentId);
        if (topic != null) payload.put("topic", topic);

        return new JSONObject(request("POST", "/guilds/" + guildId + "/channels", payload).body());
    }

    public JSONObject createChannel(String guildId, String name, int type) {
        return createChannel(guildId, name, type, null, null);
    }

    public JSONObject editChannel(String channelId, JSONObject patch) {
        return new JSONObject(request("PATCH", "/channels/" + channelId, patch).body());
    }

    public JSONObject editChannelName(String channelId, String name) {
        JSONObject patch = new JSONObject();
        patch.put("name", name);
        return editChannel(channelId, patch);
    }

    public JSONObject editChannelTopic(String channelId, String topic) {
        JSONObject patch = new JSONObject();
        patch.put("topic", topic);
        return editChannel(channelId, patch);
    }

    public JSONObject moveToCategory(String channelId, String categoryId) {
        JSONObject patch = new JSONObject();
        patch.put("parent_id", categoryId);
        return editChannel(channelId, patch);
    }

    public JSONObject moveToCategory(String channelId, String categoryId, int position) {
        JSONObject patch = new JSONObject();
        patch.put("parent_id", categoryId);
        patch.put("position", position);
        return editChannel(channelId, patch);
    }

    public JSONObject moveToRoot(String channelId) {
        JSONObject patch = new JSONObject();
        patch.put("parent_id", JSONObject.NULL);
        return editChannel(channelId, patch);
    }

    public JSONObject replaceOverwrites(String channelId, JSONArray overwrites) {
        JSONObject patch = new JSONObject();
        patch.put("permission_overwrites", overwrites);
        return editChannel(channelId, patch);
    }

    public JSONObject deleteChannel(String channelId) {
        return new JSONObject(request("DELETE", "/channels/" + channelId, null).body());
    }

    public void createOverwrite(String channelId, String overwriteId,
                                int type, String allow, String deny) {
        JSONObject payload = new JSONObject();
        payload.put("type", type);
        payload.put("allow", allow);
        payload.put("deny", deny);

        request("PUT", "/channels/" + channelId + "/permissions/" + overwriteId, payload);
    }

    public void deleteOverwrite(String channelId, String overwriteId) {
        request("DELETE", "/channels/" + channelId + "/permissions/" + overwriteId, null);
    }
}