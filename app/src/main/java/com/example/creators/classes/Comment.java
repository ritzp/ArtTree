package com.example.creators.classes;

public class Comment {
    private String commentId;
    private String userId, nickname, comment;

    public Comment(String commentId, String userId, String nickname, String comment) {
        this.commentId = commentId;
        this.userId = userId;
        this.nickname = nickname;
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getComment() {
        return comment;
    }
}
