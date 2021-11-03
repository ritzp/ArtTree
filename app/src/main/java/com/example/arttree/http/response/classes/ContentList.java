package com.example.arttree.http.response.classes;

public class ContentList {
    private String contentId;
    private String extension;
    private String title;
    private String description;
    private int views;
    private int likes;
    private String userId;
    private String nickname;

    public String getContentId() {
        return contentId;
    }

    public String getExtension() {
        return extension;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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
