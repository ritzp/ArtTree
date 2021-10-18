package com.example.arttree.http.response;

import com.example.arttree.http.response.classes.Comment;
import com.example.arttree.http.response.classes.Content;

import java.util.ArrayList;

public class ContentResponse {
    private ArrayList<Content> content;
    private ArrayList<Comment> comment;

    public ArrayList<Content> getContent() {
        return content;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }
}
