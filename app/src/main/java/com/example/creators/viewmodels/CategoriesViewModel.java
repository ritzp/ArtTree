package com.example.creators.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.creators.R;
import com.example.creators.classes.Category;

import java.util.ArrayList;

public class CategoriesViewModel extends AndroidViewModel {
    private final int[] imgs = {R.drawable.pic_photo, R.drawable.pic_drawing, R.drawable.pic_music, R.drawable.pic_video, R.drawable.pic_cartoon, R.drawable.pic_novel};
    private final String[] txts = {getApplication().getString(R.string.photo), getApplication().getString(R.string.drawing),
            getApplication().getString(R.string.music), getApplication().getString(R.string.video), getApplication().getString(R.string.cartoon),
            getApplication().getString(R.string.novel)};
    private ArrayList<Category> categoryArray;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        categoryArray = new ArrayList<>();
        for (int i=0; i<imgs.length; i++) {
            categoryArray.add(new Category(imgs[i], txts[i]));
        }
    }

    public ArrayList<Category> getCategoryArray() {
        return categoryArray;
    }
}
