package com.example.creators.classes;

import android.graphics.Bitmap;

public class Content {
    protected String contentId, title;
    protected int views;
    protected String userId, nickname;

    public Content(String contentId, String title, int views, String userId, String nickname) {
        this.contentId = contentId;
        this.title = title;
        this.views = views;
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserId() {
        return userId;
    }
}
