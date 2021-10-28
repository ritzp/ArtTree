package com.example.arttree.http.response.classes;

public class Content {
    private String contentId;
    private String extension;
    private String title;
    private String description;
    private String tag;
    private int views;
    private int likes;
    private String userId;
    private String nickname;
    private String icon;
    private boolean isLiked = false;
    private int comments;

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

    public String getTag() {
        return tag;
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

    public String getIcon() {
        return icon;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public int getComments() {
        return comments;
    }
}
