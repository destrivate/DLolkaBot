package ru.dev.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Member {
    private String nick;
    private String joinedAt;
    private String premiumSince;
    private String communicationDisabledUntil;
    private String avatar;
    private String banner;
    private boolean deaf;
    private boolean mute;
    private boolean pending;
    private int flags;
    private List<String> roles;

    public Member(JSONObject json) {
        this.roles = new ArrayList<>();
        if (json == null) return;
        this.nick = json.optString("nick", null);
        this.joinedAt = json.optString("joined_at", null);
        this.premiumSince = json.optString("premium_since", null);
        this.communicationDisabledUntil = json.optString("communication_disabled_until", null);
        this.avatar = json.optString("avatar", null);
        this.banner = json.optString("banner", null);
        this.deaf = json.optBoolean("deaf", false);
        this.mute = json.optBoolean("mute", false);
        this.pending = json.optBoolean("pending", false);
        this.flags = json.optInt("flags", 0);
        JSONArray rolesArray = json.optJSONArray("roles");
        if (rolesArray != null) {
            for (int i = 0; i < rolesArray.length(); i++) {
                String role = rolesArray.optString(i, null);
                if (role != null) {
                    this.roles.add(role);
                }
            }
        }
    }

    public String getNick() {
        return nick;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public String getPremiumSince() {
        return premiumSince;
    }

    public String getCommunicationDisabledUntil() {
        return communicationDisabledUntil;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBanner() {
        return banner;
    }

    public boolean isDeaf() {
        return deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public boolean isPending() {
        return pending;
    }

    public int getFlags() {
        return flags;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean hasRole(String roleId) {
        return roleId != null && roles.contains(roleId);
    }

    public boolean hasAnyRole(String... roleIds) {
        if (roleIds == null) return false;
        for (String r : roleIds) {
            if (roles.contains(r)) return true;
        }
        return false;
    }

    public List<String> getRolesUnmodifiable() {
        return Collections.unmodifiableList(roles);
    }

    public boolean isTimedOut() {
        return communicationDisabledUntil != null && !communicationDisabledUntil.isEmpty();
    }

    public boolean isBoosting() {
        return premiumSince != null && !premiumSince.isEmpty();
    }
}