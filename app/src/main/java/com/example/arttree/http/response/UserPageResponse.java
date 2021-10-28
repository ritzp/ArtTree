package com.example.arttree.http.response;

public class UserPageResponse {
    private String nickname;
    private String introduction;
    private String icon;
    private String header;
    private int content;
    private int likes;
    private boolean subscribed;

    public String getNickname() {
        return nickname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getIcon() {
        return icon;
    }

    public String getHeader() {
        return header;
    }

    public int getContent() {
        return content;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isSubscribed() {
        return subscribed;
    }
}
