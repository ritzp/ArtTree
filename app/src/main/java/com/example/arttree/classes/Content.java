package com.example.arttree.classes;

public class Content {
    private String contentId, extension, title, tag;
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

    public Content(String contentId, String extension, String title, String tag, int views, int likes, String userId, String nickname) {
        this.contentId = contentId;
        this.extension = extension;
        this.title = title;
        this.tag = tag;
        this.views = views;
        this.likes = likes;
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getContentId() {
        return contentId;
    }

    public String getExtension() {
        return extension;
    }

    public String getTag() {
        return tag;
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
