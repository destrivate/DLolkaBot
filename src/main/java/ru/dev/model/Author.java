package ru.dev.model;

import org.json.JSONObject;

public class Author {
    private String id;
    private String username;
    private String globalName;
    private String discriminator;
    private String avatar;
    private boolean bot;
    private int publicFlags;

    public Author(JSONObject json) {
        if (json == null) return;
        this.id = json.optString("id", null);
        this.username = json.optString("username", null);
        this.globalName = json.optString("global_name", null);
        this.discriminator = json.optString("discriminator", null);
        this.avatar = json.optString("avatar", null);
        this.bot = json.optBoolean("bot", false);
        this.publicFlags = json.optInt("public_flags", 0);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getGlobalName() {
        return globalName;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isBot() {
        return bot;
    }

    public int getPublicFlags() {
        return publicFlags;
    }

    public String getDisplayName() {
        return (globalName != null && !globalName.isEmpty()) ? globalName : username;
    }

    public String getTag() {
        if (discriminator == null || discriminator.equals("0")) {
            return username;
        }
        return username + "#" + discriminator;
    }
}