package com.example.creators.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyPageViewModel extends ViewModel {
    private MutableLiveData<String> userId;
    private MutableLiveData<String> email;
    private MutableLiveData<String> phoneNumber;
    private MutableLiveData<String> nickname;
    private MutableLiveData<String> introduction;
    private MutableLiveData<Bitmap> icon;
    private MutableLiveData<Bitmap> header;
    private MutableLiveData<Integer> contents;
    private MutableLiveData<Integer> likes;

    public MyPageViewModel() {
        try {
            this.userId = new MutableLiveData<>();
            this.email = new MutableLiveData<>();
            this.phoneNumber = new MutableLiveData<>();
            this.nickname = new MutableLiveData<>();
            this.introduction = new MutableLiveData<>();
            this.icon = new MutableLiveData<>();
            this.header = new MutableLiveData<>();
            this.contents = new MutableLiveData<>();
            this.likes = new MutableLiveData<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<String> getUserId() {
        return userId;
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
}
