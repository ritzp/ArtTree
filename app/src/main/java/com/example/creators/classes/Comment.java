package com.example.creators.classes;

import android.graphics.Bitmap;

public class Comment {
    private String nickname, comment;
    private Bitmap icon;

    public Comment(String nickname, Bitmap icon, String comment) {
        this.nickname = nickname;
        this.icon = icon;
        this.comment = comment;
    }

    public String getNickname() {
        return nickname;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getComment() {
        return comment;
    }
}
