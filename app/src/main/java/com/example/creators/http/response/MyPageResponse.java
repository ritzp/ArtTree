package com.example.creators.http.response;

public class MyPageResponse {
    private String nickname;
    private String introduction;
    private String icon;
    private String header;
    private int content;
    private int likes;

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
}
