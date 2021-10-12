package com.example.creators.classes;

import android.graphics.Bitmap;

public class HomeContent {
    private String category;
    private String title;
    private String description;
    private Bitmap icon;
    private String nickname;
    private Bitmap content;

    public HomeContent(String category, String title, String description, Bitmap icon, String nickname) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.nickname = nickname;
    }

    public HomeContent(String category, String title, String description, Bitmap icon, String nickname, Bitmap content) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.nickname = nickname;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getNickname() {
        return nickname;
    }

    public Bitmap getContent() {
        return content;
    }
}
