package com.example.creators.classes;

import android.media.Image;

public class Category {
    private int img;
    private String txt;

    public Category(int img, String txt) {
        this.img = img;
        this.txt = txt;
    }

    public int getImg() {
        return img;
    }

    public String getTxt() {
        return txt;
    }
}
