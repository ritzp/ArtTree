package com.example.arttree.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserPageViewModel extends ViewModel {
    private MutableLiveData<String> userId;
    private MutableLiveData<String> nickname;
    private MutableLiveData<String> introduction;
    private MutableLiveData<Integer> content;
    private MutableLiveData<Integer> likes;

    public UserPageViewModel() {
        try {
            this.userId = new MutableLiveData<>();
            this.nickname = new MutableLiveData<>();
            this.introduction = new MutableLiveData<>();
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

    public MutableLiveData<Integer> getContent() {
        return content;
    }

    public MutableLiveData<Integer> getLikes() {
        return likes;
    }
}
