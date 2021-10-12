package com.example.creators.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.creators.R;
import com.example.creators.classes.Category;
import com.example.creators.main_fragments.CategoriesFragment;

import java.util.ArrayList;

public class CategoriesViewModel extends ViewModel {
    private final int[] imgs = {R.drawable.pic_photo, R.drawable.pic_drawing, R.drawable.pic_music, R.drawable.pic_video, R.drawable.pic_cartoon, R.drawable.pic_novel};
    private final String[] txts = {CategoriesFragment.context.getString(R.string.photo), CategoriesFragment.context.getString(R.string.drawing),
        CategoriesFragment.context.getString(R.string.music),CategoriesFragment.context.getString(R.string.video),
            CategoriesFragment.context.getString(R.string.cartoon), CategoriesFragment.context.getString(R.string.novel)};
    private ArrayList<Category> categoryArray;

    public CategoriesViewModel() {
        super();
        categoryArray = new ArrayList<>();
        for (int i=0; i<imgs.length; i++) {
            categoryArray.add(new Category(imgs[i], txts[i]));
        }
    }

    public ArrayList<Category> getCategoryArray() {
        return categoryArray;
    }
}
