package ru.dev.context;

import org.json.JSONObject;

public class MessageContext {
    private String id;
    private String channelId;
    private String guildId;
    private String content;
    private String timestamp;
    private boolean tts;
    private boolean pinned;
    private int type;
    private int flags;
    private JSONObject author;
    private JSONObject member;

    public MessageContext(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.channelId = jsonObject.optString("channel_id");
        this.guildId = jsonObject.optString("guild_id");
        this.content = jsonObject.optString("content");
        this.timestamp = jsonObject.optString("timestamp");
        this.tts = jsonObject.optBoolean("tts", false);
        this.pinned = jsonObject.optBoolean("pinned", false);
        this.type = jsonObject.optInt("type", 0);
        this.flags = jsonObject.optInt("flags", 0);
        this.author = jsonObject.optJSONObject("author");
        this.member = jsonObject.optJSONObject("member");
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getContent() {
        return content;
    }


    public String getTimestamp() {
        return timestamp;
    }


    public boolean isTts() {
        return tts;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public boolean isPinned() {
        return pinned;
    }
}
