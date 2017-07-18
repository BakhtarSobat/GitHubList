package com.bsobat.github.dto;

import android.arch.persistence.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class Owner {
    @ColumnInfo(name = "user_id")
    private String id;
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public void setId(String id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }
}
