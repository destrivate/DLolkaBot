package ru.dev.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReferencedMessage {
    private String id;
    private String channelId;
    private String guildId;
    private String content;
    private String timestamp;
    private String editedTimestamp;

    private boolean tts;
    private boolean pinned;
    private boolean mentionEveryone;

    private int type;
    private int flags;

    private Author author;
    private Member member;

    private List<String> mentionRoles;
    private List<JSONObject> mentions;
    private List<JSONObject> attachments;
    private List<JSONObject> embeds;
    private List<JSONObject> components;

    public ReferencedMessage(JSONObject json) {
        this.mentionRoles = new ArrayList<>();
        this.mentions = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.embeds = new ArrayList<>();
        this.components = new ArrayList<>();

        if (json == null) return;

        this.id = json.optString("id", null);
        this.channelId = json.optString("channel_id", null);
        this.guildId = json.optString("guild_id", null);
        this.content = json.optString("content", null);
        this.timestamp = json.optString("timestamp", null);
        this.editedTimestamp = json.optString("edited_timestamp", null);

        this.tts = json.optBoolean("tts", false);
        this.pinned = json.optBoolean("pinned", false);
        this.mentionEveryone = json.optBoolean("mention_everyone", false);

        this.type = json.optInt("type", 0);
        this.flags = json.optInt("flags", 0);

        JSONObject authorJson = json.optJSONObject("author");
        this.author = authorJson != null ? new Author(authorJson) : null;

        JSONObject memberJson = json.optJSONObject("member");
        this.member = memberJson != null ? new Member(memberJson) : null;

        JSONArray rolesArray = json.optJSONArray("mention_roles");
        if (rolesArray != null) {
            for (int i = 0; i < rolesArray.length(); i++) {
                String role = rolesArray.optString(i, null);
                if (role != null) this.mentionRoles.add(role);
            }
        }

        this.mentions = toList(json.optJSONArray("mentions"));
        this.attachments = toList(json.optJSONArray("attachments"));
        this.embeds = toList(json.optJSONArray("embeds"));
        this.components = toList(json.optJSONArray("components"));
    }

    private static List<JSONObject> toList(JSONArray array) {
        List<JSONObject> list = new ArrayList<>();
        if (array == null) return list;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj != null) list.add(obj);
        }
        return list;
    }

    public String getId() { return id; }
    public String getChannelId() { return channelId; }
    public String getGuildId() { return guildId; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
    public String getEditedTimestamp() { return editedTimestamp; }

    public boolean isTts() { return tts; }
    public boolean isPinned() { return pinned; }
    public boolean isMentionEveryone() { return mentionEveryone; }

    public int getType() { return type; }
    public int getFlags() { return flags; }

    public Author getAuthor() { return author; }
    public Member getMember() { return member; }

    public List<String> getMentionRoles() { return mentionRoles; }
    public List<JSONObject> getMentions() { return mentions; }
    public List<JSONObject> getAttachments() { return attachments; }
    public List<JSONObject> getEmbeds() { return embeds; }
    public List<JSONObject> getComponents() { return components; }

    public String getAuthorName() {
        return author != null ? author.getDisplayName() : null;
    }
}