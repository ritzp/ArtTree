package com.example.arttree.classes;

public class Content {
    private String contentId, title;
    private int views, likes;
    private String userId, nickname;

    public Content(String contentId, String title, int views, int likes, String userId, String nickname) {
        this.contentId = contentId;
        this.title = title;
        this.views = views;
        this.likes = likes;
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getContentId() {
        return contentId;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
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
