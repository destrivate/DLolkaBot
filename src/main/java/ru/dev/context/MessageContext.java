package ru.dev.context;

import org.json.JSONException;
import org.json.JSONObject;
import ru.dev.model.Author;
import ru.dev.model.Member;
import ru.dev.model.ReferencedMessage;

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
    private Author author;
    private Member member;
    private ReferencedMessage referencedMessage;

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
        this.author = new Author(jsonObject.getJSONObject("author"));
        this.member = new Member(jsonObject.getJSONObject("author"));
        try {
            this.referencedMessage = new ReferencedMessage(jsonObject.getJSONObject("referenced_message"));
        } catch (JSONException e) {
            this.referencedMessage = null;
        }

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

    public Author getAuthor() {
        return author;
    }

    public ReferencedMessage getReferencedMessage(){
        return referencedMessage;
    }

    public Member getMember() {
        return member;
    }
}
