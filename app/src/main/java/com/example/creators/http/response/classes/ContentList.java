package com.example.creators.http.response.classes;

public class ContentList {
    private String contentId;
    private String title;
    private int views;
    private int likes;
    private String userId;
    private String nickname;

    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }
}
