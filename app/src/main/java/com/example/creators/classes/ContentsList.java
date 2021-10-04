package com.example.creators.classes;

import android.graphics.Bitmap;

public class ContentsList {
    protected String contentsId, title;
    protected int views;
    protected String userId, nickname;
    protected Bitmap icon;

    public ContentsList(String contentsId, String title, int views, String userId, String nickname, Bitmap icon) {
        this.contentsId = contentsId;
        this.title = title;
        this.views = views;
        this.userId = userId;
        this.nickname = nickname;
        this.icon = icon;
    }

    public String getContentsId() {
        return contentsId;
    }

    public String getTitle() {
        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
