package com.example.creators.classes;

import android.graphics.Bitmap;

public class MyContent {
    private String contentId, title;
    private int category;

    public MyContent(String contentId, String title, int category) {
        this.contentId = contentId;
        this.title = title;
        this.category = category;
    }

    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public int getCategory() {
        return category;
    }
}
