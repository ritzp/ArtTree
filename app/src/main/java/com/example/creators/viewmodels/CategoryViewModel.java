package com.example.creators.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.creators.R;
import com.example.creators.classes.Category;

import java.util.ArrayList;

public class CategoryViewModel extends AndroidViewModel {
    private final int[] imgs = {R.drawable.ic_picture, R.drawable.ic_headset, R.drawable.ic_video, R.drawable.ic_document_written};
    private final String[] txts = {getApplication().getString(R.string.image), getApplication().getString(R.string.sound),
            getApplication().getString(R.string.video), getApplication().getString(R.string.text)};
    private ArrayList<Category> categoryArray = new ArrayList<>();

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        for (int i=0; i<imgs.length; i++) {
            categoryArray.add(new Category(imgs[i], txts[i]));
        }
    }

    public ArrayList<Category> getCategoryArray() {
        return categoryArray;
    }
}
