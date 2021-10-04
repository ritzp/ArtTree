package com.example.creators.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContentsDetailViewModel extends ViewModel {
    private MutableLiveData<String> contentsId;
    private MutableLiveData<String> title;
    private MutableLiveData<String> description;
    private MutableLiveData<Integer> views;
    private MutableLiveData<Integer> likes;
    private MutableLiveData<String> userId;
    private MutableLiveData<String> nickname;
    private MutableLiveData<Bitmap> icon;
    private MutableLiveData<Integer> comments;

    public ContentsDetailViewModel() {
        try {
            this.contentsId = new MutableLiveData<>();
            this.title = new MutableLiveData<>();
            this.description = new MutableLiveData<>();
            this.views = new MutableLiveData<>();
            this.likes = new MutableLiveData<>();
            this.userId = new MutableLiveData<>();
            this.nickname = new MutableLiveData<>();
            this.icon = new MutableLiveData<>();
            this.comments = new MutableLiveData<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<String> getContentsId() {
        return contentsId;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<Integer> getViews() {
        return views;
    }

    public MutableLiveData<Integer> getLikes() {
        return likes;
    }

    public MutableLiveData<String> getUserId() {
        return userId;
    }

    public MutableLiveData<String> getNickname() {
        return nickname;
    }

    public MutableLiveData<Bitmap> getUserIcon() {
        return icon;
    }

    public MutableLiveData<Integer> getComments() {
        return comments;
    }
}
