package com.example.creators.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.creators.R;

public class CategoryListViewModel extends AndroidViewModel {
    private final String[] imageCategories = {getApplication().getString(R.string.photo), getApplication().getString(R.string.icon),
            getApplication().getString(R.string.illustration), getApplication().getString(R.string.image_etc)};
    private final String[] soundCategories = {getApplication().getString(R.string.music), getApplication().getString(R.string.bgm),
            getApplication().getString(R.string.se), getApplication().getString(R.string.sound_etc)};
    private final String[] videoCategories = {getApplication().getString(R.string.recorded_video), getApplication().getString(R.string.animation),
            getApplication().getString(R.string.video_etc)};
    private final String[] textCategories = {getApplication().getString(R.string.novel), getApplication().getString(R.string.poem),
            getApplication().getString(R.string.text_etc)};

    public CategoryListViewModel(@NonNull Application application) {
        super(application);
    }

    public String[] getImageCategories() {
        return imageCategories;
    }

    public String[] getSoundCategories() {
        return soundCategories;
    }

    public String[] getVideoCategories() {
        return videoCategories;
    }

    public String[] getTextCategories() {
        return textCategories;
    }
}
