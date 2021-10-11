package com.example.creators.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyPageViewModel extends ViewModel {
    private MutableLiveData<String> userId;
    private MutableLiveData<String> nickname;
    private MutableLiveData<String> introduction;
    private MutableLiveData<Bitmap> icon;
    private MutableLiveData<Bitmap> header;
    private MutableLiveData<Integer> content;
    private MutableLiveData<Integer> likes;

    public MyPageViewModel() {
        try {
            this.userId = new MutableLiveData<>();
            this.nickname = new MutableLiveData<>();
            this.introduction = new MutableLiveData<>();
            this.icon = new MutableLiveData<>();
            this.header = new MutableLiveData<>();
            this.content = new MutableLiveData<>();
            this.likes = new MutableLiveData<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<String> getUserId() {
        return userId;
    }

    public MutableLiveData<String> getNickname() {
        return nickname;
    }

    public MutableLiveData<String> getUserIntroduction() {
        return introduction;
    }

    public MutableLiveData<Bitmap> getUserIcon() {
        return icon;
    }

    public MutableLiveData<Bitmap> getUserHeader() {
        return header;
    }

    public MutableLiveData<Integer> getContent() {
        return content;
    }

    public MutableLiveData<Integer> getLikes() {
        return likes;
    }
}
